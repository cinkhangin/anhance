# anhance

An Android library to make life easier.

This Library has many useful Functions to solve problems easier and faster.

## Fetures
### Activity extensions

```kotlin
showToast("hi mom!") //show a toast
showKeyboard() //show the keyboard
hideKeyboard() //hide the keyboard
```

### Extensions for Date and time
```kotlin
val threeSeconds = 3.sec  // 3000L
val three = threeSeconds.toSecond // 3L
val currentMillis = millisNow //Current millisecond
val date = millisNow.formatWith("dd/MM/yyyy") //format date
val duration = threeSeconds.formatDuration() //3s
val timer = threeSeconds.formatTimer() //00:03

```

### Context extensions
```kotlin
context.showToast("hi mom!")
context.isInternetAvailable //check connection
```

### Extensions for dimensions
```kotlin
val padding = 16.toPx //convert 12dp to px
val margin = 24.toDp //convert 24px to dp
```

### Fragment extensions
```kotlin
showToast("hi mom!")
setStatusBarColorResource(R.color.primary) //change status bar's color
val color = getColor(R.color.primary) //change resource color into color
setStatusBarColor(color) //change status bar's color
showKeyboard() //show keyboard
hideKeyboard() //hide keyboard
showLoadingDialog("Loading...") //show a loading dialog
dismissLoadingDialog() //dismiss the loading dialog
openGallery{ uri -> //open device image picker 
    // ...
}
```

### `AnStore` for easy DataStore Setup

```kotlin
//inside coroutine scope
val int = AnStore(context).readInt("level", 0)
```

### `AnDayNight` for easy App Theme

```kotlin
runLightTheme() //set Light theme
runNightTheme() //set Dark theme
//why night but not dark? because light/night
//if you want to change theme and save it
context.installLightTheme() 
context.installNightTheme()
context.installSystemTheme() //follow system
//get current theme
getCurrentTheme()
context.isNightMode()
context.isLightMode()
```

### Extensions for files
```kotlin
//getting file extension from a Uri
val fileExtension = uri.fileExtension(context)
```

### String extensions
```kotlin
val a = "100".toSafeInt() //100 safely convert string into int
val b = "abc".toSafeInt() //0
"hello mom".copy(context) //copy the string to clipboard
"<font color=#ff0000>red</font>".toHtml() //string into html
"12bdae".generateMore() //generate more random combination
"fnck you".censor() //censor a string //**** you
```

## Implementation
### Gradle

- Add the Jitpack repository :

```groovy
repositories {
  maven { url 'https://jitpack.io' }
}
```

add a dependency
```kotlin
dependencies {
    implementation("com.github.cinkhangin:anhance:0.0.13")
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
```
```xml
<dependency>
  <groupId>com.github.cinkhangin</groupId>
  <artifactId>anhance</artifactId>
  <version>0.0.13</version>
</dependency>
```