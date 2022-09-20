# anhance

An Android library to make life easier.

This Library has many useful Functions to solve problems easier and faster.

## Fetures
### `AnActivity` for Activity extensions
```kotlin
class MainActivity : AppCompatActivity{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showToast("hi mom!")
    }
}
```

### `AnClock` for better looking Code for time
```kotlin
val threeMinutes = 3.min
val oneHundredYears = 100.year
```

### `AnContext` for Context extensions
```kotlin
context.showToast("hi mom!")
```

### `AnFragment` for Fragment extensions

```kotlin
class MainFragment : Fragment(R.layout.fragment_main) {
    private var viewBinding: FragmentMainBinding? = null
    private val binding get() = viewBinding!!
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentMainBinding.bind(view)
        
        showToast("hi mom!")
    }
}
```

### `AnStore` for easy DataStore Setup

```kotlin
//inside coroutine scope
val int = AnStore(context).readInt("level", 0)
```

### `AnDateTime` for DateTime utils

Use `AnDateTime` to for DateTime utils.

### `AnString` for String extensions

use String extension function `toSafeInt` 

```kotlin
val a = "100".toSafeInt() //100
val b = "abc".toSafeInt() //0
```

### `AnText` for Text utils

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
  implementation 'com.github.cinkhangin:anhance:0.0.7-beta'
}
```
```groovy
dependencies {
    implementation("com.github.cinkhangin:anhance:0.0.7-beta")
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
  <version>0.0.7-beta</version>
</dependency>
```