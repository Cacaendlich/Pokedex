plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.pokedex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokedex"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    val roomVersion = "2.6.1"

    implementation("androidx.test:core-ktx:1.6.0")
    implementation("androidx.test.ext:junit-ktx:1.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    // Kotlin coroutines para facilitar a programação assíncrona
    implementation("androidx.core:core-ktx:1.13.1")

    // Biblioteca do Android Jetpack para integração com Jetpack Compose
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Bibliotecas de suporte do Android para compatibilidade com versões antigas
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Anotações de suporte do Android
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Testes unitários e de instrumentação
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    testImplementation ("io.mockk:mockk:1.12.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    testImplementation ("androidx.test:core:1.6.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Extensões do Android Jetpack para o ciclo de vida
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // RecyclerView para exibição de listas
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Retrofit para comunicação de rede
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Glide para carregamento de imagens
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //biblioteca de processamento de imagem
    implementation("androidx.palette:palette-ktx:1.0.0")

    implementation("androidx.compose.material3:material3:1.2.1")

    // Kotlin
    implementation("androidx.fragment:fragment-ktx:1.8.0")

    //Room
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")
    implementation("androidx.room:room-common")

    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")

}