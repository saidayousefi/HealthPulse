plugins {
    id("com.android.application")

}




android {
    namespace = "com.example.healthtracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healthtracker"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Enable MultiDex support if needed
        multiDexEnabled = true


    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    // Lint options to help with code quality
    lint {
        abortOnError = false
    }
}



dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Navigation components
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    // Room components
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    // Material Design components
    implementation ("com.google.android.material:material:1.12.0")

    // WorkManager for scheduling tasks
    implementation ("androidx.work:work-runtime:2.9.0")

    // ThreeTenABP for handling date and time
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.7")

    // Preferences library
    implementation ("androidx.preference:preference:1.2.1")

    implementation ("androidx.gridlayout:gridlayout:1.0.0")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

}
