# Testing Anhance

Run the fast regression suite from the repository root:

```bash
./gradlew :anhance:testDebugUnitTest
```

The suite covers numeric and string edge cases, duration conversions and formatting,
collection selection and copying, random bounds, null/result helpers, coroutine
collection and cancellation, overlapping toast lifetimes, assets, clipboard,
permission checks and external app intents,
connectivity, colors, HTML, dimensions, image saving and cleanup,
document persistence and failure handling,
audio preparation and resource ownership, speech recognition events, and
text-to-speech initialization and teardown.

Android framework tests use [Robolectric](https://robolectric.org/getting-started/)
on API 28 and 35, with legacy document tests on the minimum supported API 26.
Document provider tests use a file-backed test provider on API 35
to exercise real resolver streams while controlling insert, write, update, and
delete failures. Coroutine tests use virtual time; they do not sleep. Random tests
assert bounds and alphabet contracts rather than expecting particular samples.
Image tests use native graphics so invalid images exercise real codec behavior.

Run the build and static checks before accepting a change:

```bash
./gradlew :anhance:testDebugUnitTest :anhance:lintDebug :anhance:lintRelease :anhance:assembleRelease :anhance:assembleDebugAndroidTest :example:assembleDebug
```

The GitHub Actions workflow runs these checks on pushes and pull requests. Its
Java 21 and Android SDK 37 setup matches the current build. It does not publish
the library. Running a hosted workflow requires committing and pushing the files.

To check behavior against Android itself, start a device or emulator and run:

```bash
./gradlew :anhance:connectedDebugAndroidTest
```

These tests exercise private-file document saving, renaming and deletion,
MediaStore document operations on API 29+, and local media playback state
transitions. They use test-owned temporary files and remove them afterward.
The MP3 fixture comes from the example app.

The integration test APK compiled successfully during this audit, but its device
execution was not validated: the existing emulator profile was locked, and a
fresh emulator required more disk space than was available.

Reports are generated under `anhance/build/reports/tests/testDebugUnitTest/`,
`anhance/build/reports/androidTests/connected/`, and
`anhance/build/reports/lint-results-debug.html`.

For every bug fix, add a regression that fails for the old behavior. Cover the
observable public contract, successful and failed operations, boundary inputs,
and cancellation or teardown where applicable. Keep framework integration tests
for behavior that shadows cannot establish.

No suite can guarantee every future change is safe. Speech services, microphone
permissions, real network streams, storage providers from other vendors, and
audible text-to-speech quality still need device checks. `SimpleSoundPlayer`
currently contains placeholder methods and has no usable playback contract.

## Corrected behavior to account for when upgrading

- `safeDiv` substitutes one for a zero divisor and preserves the numerator's sign.
- `toSafeInt` accepts signed integers and returns zero for invalid or overflowing input.
- `caseContains` ignores case. Email validation rejects malformed domains and whitespace.
- A week is exactly seven days. `formatClock` includes the full clock; timers
  separate year/day prefixes. Negative numbers are not zero-padded.
- Unsupported-only random string examples are returned unchanged.
- `isInternetAvailable` requires internet and validated network capabilities,
  including Ethernet and VPN connections. A local Wi-Fi connection alone is insufficient.
- Document creation returns the provider's content URI on API 29+. Saving updates
  the cache only after writing succeeds. Renaming changes the existing document in
  place and preserves the original on failure. The existing nested rename result
  is retained for compatibility: `renameFile(...).getOrThrow().getOrThrow()`.
- Use `DocumentCreator.deleteFile(context, document)` for content URIs. The original
  overload accepts file URIs. Failed deletions retain cached documents.
- `DocumentLoader.load()` reads the legacy public Documents directory. It does not
  enumerate scoped-storage content URIs; created and saved documents enter the cache
  through `DocumentCreator`.
- Media players wait for asynchronous preparation and release resources when replaced,
  removed, or cleared. Speech final results use `Result` and the best hypothesis.
- Reading before text-to-speech initialization reports an error; teardown resets its state.
- Image saves close streams, publish completed MediaStore rows, remove failed writes,
  and report failure when insertion, compression, or publication fails.
- External app helpers support application contexts and encode usernames or IDs
  so they cannot accidentally change the URI's query or path structure.
