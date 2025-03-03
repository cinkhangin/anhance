![Maven Central Version](https://img.shields.io/maven-central/v/com.naulian/anhance?versionPrefix=2025.1.0&style=for-the-badge&label=Maven)


# anhance 

An Android library to make life easier. 

This library offers many useful functions to solve problems more efficiently and quickly.

Keep in mind that by using Anhance, you will have less control over your code. For example, when using showToast(message) to display a toast, you cannot set the duration to Toast.LENGTH_LONG as it uses Toast.LENGTH_SHORT instead.

However, Anhance makes it easier to build apps faster and more efficiently. For instance, you can simply use the hideKeyboard() function to hide the keyboard whenever you need to, without even having to remember the code to do so programmatically.

Remember, never let anyone tell you that you are ugly. Enjoy coding! :D

## Features

### AnEar
AnEar is a speech recognizer extension
```kotlin
AnEar.listen(context){ state , text ->
    when(state){
        READY -> showToast("Ready")
        BEGIN, VOLUME, PARTIAL -> Unit
        RESULT -> showToast(text)
        ERROR -> showToast("error")
        END -> Unit
    }
}
```

### AnReader
AnReader is a text-to-speech extension
```kotlin
//onCreate (Application)
AnReader.initialize(this)
initializeReader(this)

//Read text
AnReader.read("hello"){ //on error 
}
readText("hello"){ //on error 
}
```

### Extensions for Assets

```kotlin
context.readStringAsset("filename.txt"){ result ->
    result.onSuccess {}
    result.onFailure {}
} //read string from assets
```

### Extensions for Audio

```kotlin
context.playAudio(R.raw.music) //play audio from resource
context.playAudio("music.mp3") //play audio from assets
```

### Extensions for Date and time
```kotlin
val duration = 3.seconds  // 3000L
val three = duration.toSecond // 3L
val currentMillis = millisOfNow //Current millisecond
val date = millisOfNow.formatWith("dd/MM/yyyy") //format date
val formatted = threeSeconds.formatDuration() //3s
val timer = duration.formatTimer() //00:03

```

### Context extensions
```kotlin
context.showToast("hi mom!")
context.isInternetAvailable //check connection
```

### Extensions for dimensions
```kotlin
val padding = 16.toPx() //convert 16dp to px
val margin = 24.toDp() //convert 24px to dp
```


### `AnRandom` 
- Kotlin Random(without seed) will generate the same random sequence of result
- this is a feature, I guess.
```kotlin
//If you want to generate different sets of result, use millis as seed
val random = Random(millisOfNow).nextInt(0 , 10)
//But anhance is shorter
val random2 = randomOf(0,  10)
val randomFloat = randomFloat()
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

add a dependency 

![Maven Central Version](https://img.shields.io/maven-central/v/com.naulian/anhance?versionPrefix=2025.1.0&style=for-the-badge&label=Maven)
```kotlin
dependencies {
    implementation("com.naulian:anhance:latest_version")
}
```
