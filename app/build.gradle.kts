plugins {
    // Plugin principal para aplicaciones Android. Define que este módulo es una App ejecutable.
    alias(libs.plugins.android.application)
    
    // Kotlin Symbol Processing (KSP): Motor moderno para generar código.
    // Lo usamos para que Room genere automáticamente las implementaciones de nuestras interfaces DAO.
    alias(libs.plugins.google.devtools.ksp)
    
    // Plugin de Room: Facilita la configuración de la base de datos y la exportación de esquemas JSON.
    alias(libs.plugins.androidx.room)
}

android {
    // Identificador del paquete de código fuente.
    namespace = "com.example.tasksyncfinal"
    
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        // ID único de la aplicación en el sistema Android y Play Store.
        applicationId = "com.example.tasksyncfinal"
        
        // Versión mínima de Android requerida (Android 7.0).
        minSdk = 24
        
        // Versión de Android para la cual la app está optimizada (Android 15).
        targetSdk = 36
        
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        // VIEW BINDING: Habilita el acceso directo a las vistas del XML desde Kotlin sin 'findViewById'.
        // Genera clases automáticas como 'ActivityLoginBinding'.
        viewBinding = true
    }
}

room {
    // Directorio donde Room guardará archivos JSON con la estructura de la base de datos.
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // --- LIBRERÍAS BASE DE ANDROID UI ---
    implementation(libs.androidx.core.ktx) // Funciones extendidas de Kotlin para Android.
    implementation(libs.androidx.appcompat) // Componentes visuales compatibles con versiones antiguas.
    implementation(libs.material) // Componentes de Material Design 3 (Google).
    implementation(libs.androidx.activity) // Integración con el ciclo de vida de la Activity.
    implementation(libs.androidx.constraintlayout) // Sistema de diseño flexible por restricciones.

    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- ROOM (Base de Datos Local) ---
    implementation(libs.androidx.room.runtime) // Librería base de Room.
    implementation(libs.androidx.room.ktx) // Extensiones de Kotlin para usar Corrutinas en la DB.
    ksp(libs.androidx.room.compiler) // Compilador que genera el código interno de Room.

    // --- RETROFIT & NETWORKING (API) ---
    // Retrofit: Cliente HTTP para conectar con servicios web RESTful.
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    
    // Converter GSON: Plugin de Retrofit para convertir JSON en clases de datos Kotlin.
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    
    // GSON: Librería para procesar JSON de forma eficiente.
    implementation("com.google.code.gson:gson:2.10.1")

    // OKHTTP: El motor de red que usa Retrofit.
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // LOGGING INTERCEPTOR: Permite ver en la consola (Logcat) todo el tráfico de red (URLs, JSONs).
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- CORRUTINAS (Asincronía) ---
    // Permite ejecutar procesos pesados en hilos secundarios sin bloquear la pantalla del usuario.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // --- GLIDE (Imágenes) ---
    // Librería estándar para descargar e insertar imágenes desde internet en ImageViews.
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
