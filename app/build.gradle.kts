
import java.util.Base64
import java.util.Locale

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.dashimaki_dofu.mytaskmanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dashimaki_dofu.mytaskmanagement"
        minSdk = 29
        targetSdk = 34
        versionCode = 12
        versionName = "0.10.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    val releaseSigningConfigName = "release_signing_config"
    signingConfigs {
        create(releaseSigningConfigName) {
            val releaseKeystoreFileName = "release-keystore.jks"
            if (System.getenv("ENV_SIGN_KEYSTORE_BASE64") != null) {
                System.getenv("ENV_SIGN_KEYSTORE_BASE64").let { base64 ->
                    val decoder = Base64.getMimeDecoder()
                    File(releaseKeystoreFileName).also { file ->
                        file.createNewFile()
                        file.writeBytes(decoder.decode(base64))
                    }
                }
            }
            storeFile = rootProject.file(releaseKeystoreFileName)
            storePassword = System.getenv("ENV_SIGN_STORE_PASSWORD")
            keyAlias = System.getenv("ENV_SIGN_KEY_ALIAS")
            keyPassword = System.getenv("ENV_SIGN_KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName(releaseSigningConfigName)
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
    val navVersion = "2.7.7"
    val roomVersion = "2.6.1"
    val lifecycleVersion = "2.7.0"
    val composeRuntimeVersion = "1.6.1"
    val hiltVersion = "2.48"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${lifecycleVersion}")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata:${composeRuntimeVersion}")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha08")
    implementation("androidx.navigation:navigation-compose:${navVersion}")
    implementation("androidx.room:room-runtime:${roomVersion}")
    implementation("androidx.room:room-ktx:${roomVersion}")
    annotationProcessor("androidx.room:room-compiler:${roomVersion}")
    kapt("androidx.room:room-compiler:${roomVersion}")  // KSPに置き換えたいが、エラーが解決できないため一旦kaptで
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.2")
    implementation("com.google.firebase:firebase-analytics-ktx:21.5.1")
    implementation("com.google.firebase:firebase-appdistribution-api-ktx:16.0.0-beta12")
    implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta12")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

class Version(private var code: Int, version: String) {
    private var major: Int
    private var minor: Int
    private var patch: Int

    init {
        val (major, minor, patch) = version.split(".").map { it.toInt() }
        this.major = major
        this.minor = minor
        this.patch = patch
    }

    val functionsByName = listOf(::bumpMajor, ::bumpMinor, ::bumpPatch).associateBy { it.name }

    @SuppressWarnings("unused")
    fun bumpMajor() {
        major += 1
        minor = 0
        patch = 0

        code += 1
    }

    @SuppressWarnings("unused")
    fun bumpMinor() {
        minor += 1
        patch = 0

        code += 1
    }

    @SuppressWarnings("unused")
    fun bumpPatch() {
        patch += 1

        code += 1
    }

    fun getName(): String = "$major.$minor.$patch"
    fun getCode(): Int = code
}

tasks.addRule("Pattern: bump<TYPE>Version") {
    if (this.matches(Regex("bump(Major|Minor|Patch)Version"))) {
        task(this) {
            doLast {
                // コマンドから実行タイプを抽出
                val type = this@addRule
                    .replace(Regex("bump"), "")
                    .replace(Regex("Version"), "")

                println("Bumping ${type.lowercase(Locale.getDefault())} version...")

                // 旧バージョンを取得
                val oldVersionCode = android.defaultConfig.versionCode ?: return@doLast
                val oldVersionName = android.defaultConfig.versionName ?: return@doLast
                val version = Version(oldVersionCode, oldVersionName)

                // メソッド名を取得しそのメソッドを実行（実行不可であればエラー）
                val methodName = "bump${type}"
                version.functionsByName[methodName]?.invoke() ?: error("Unknown method: $methodName")

                // 更新したバージョンを取得
                val newVersionCode = version.getCode()
                val newVersionName = version.getName()

                println("${oldVersionName}($oldVersionCode) -> ${newVersionName}($newVersionCode)")

                // ビルドファイルに更新したversionName・versionCodeを書き込み
                var updated = buildFile.readText()
                updated = updated.replaceFirst(
                    "versionName = \"${oldVersionName}\"",
                    "versionName = \"${newVersionName}\""
                )
                updated = updated.replaceFirst(
                    "versionCode = $oldVersionCode",
                    "versionCode = $newVersionCode"
                )
                buildFile.writeText(updated)
            }
        }
    }
}

tasks.register("printVersionCode") {
    doLast {
        println(android.defaultConfig.versionCode)
    }
}

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}
