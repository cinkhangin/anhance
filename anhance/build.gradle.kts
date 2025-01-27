import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = 35
    namespace = "com.naulian.anhance"

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    //navigation
    val navVersion = "2.8.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //lifecycle
    val lifecycleVersion = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    //datetime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)

    signAllPublications()

    coordinates(
        groupId = "com.naulian",
        artifactId = "anhance",
        version = "2025.1.0"
    )
    //./gradlew publishAndReleaseToMavenCentral --no-configuration-cache

    pom {
        name.set("Anhance")
        description.set("Android extensions library to make life easier")
        inceptionYear.set("2023")
        url.set("https://github.com/cinkhangin/anhance/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("naulian")
                name.set("Naulian")
                url.set("https://github.com/cinkhangin/")
                email.set("hello@naulian.com")
            }
        }
        scm {
            url.set("https://github.com/cinkhangin/anhance/")
            connection.set("scm:git:git://github.com/cinkhangin/anhance.git")
            developerConnection.set("scm:git:ssh://git@github.com/cinkhangin/anhance.git")
        }
    }
}