# anhance

An Android library to make life easier.

## About

Use `TimeUtil` to format time.

### safely convert string into int

use String extension function `toSafeInt` 

```kotlin
val a = "100".toSafeInt() //100
val b = "abc".toSafeInt() //0
```

## Implementation
### Gradle

- Add the Jitpack repository :

```kotlin
repositories {
  maven { url 'https://jitpack.io' }
}
```

add a dependency
```kotlin
dependencies {
  implementation 'com.github.cinkhangin:anhance:0.0.3-beta'
}
```

- To use the library in a single-platform project, add a dependency to the dependencies block.

```groovy
dependencies {
    implementation("com.github.cinkhangin:anhance:0.0.3-beta")
}
```

### Maven

Add a dependency to the `<dependencies>` element. Note that you need to use the platform-specific `-jvm` artifact in Maven.

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.cinkhangin</groupId>
  <artifactId>anhance</artifactId>
  <version>0.0.3-beta</version>
</dependency>
```