import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

val localProperties =
    Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        }
    }

// API 서버 주소 (Single Source of Truth — 여기만 수정)
val apiHost = "43.203.62.97"
val apiPort = "8080"

android {
    namespace = "com.hm.picplz"
    compileSdk = 35

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.hm.picplz"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.4"

        testInstrumentationRunner = "com.google.dagger.hilt.android.testing.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "${localProperties["kakao_native_app_key"]}",
        )
        buildConfigField("String", "KAKAO_REST_API_KEY", "${localProperties["kakao_rest_api_key"]}")
        buildConfigField("String", "DEV_GUEST_TOKEN", "${localProperties["dev_guest_token"]}")
        buildConfigField("String", "DEV_USER_TOKEN", "${localProperties["dev_user_token"]}")
        buildConfigField("String", "API_BASE_URL", "\"http://$apiHost:$apiPort\"")

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = localProperties["kakao_native_app_key"] ?: ""
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("boolean", "DEV_MODE", "true")
            buildConfigField("boolean", "STAGING_MODE", "false")
            resValue("string", "app_name", "[DEV] picplz")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("boolean", "DEV_MODE", "false")
            buildConfigField("boolean", "STAGING_MODE", "true")
            resValue("string", "app_name", "[STG] picplz")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("boolean", "DEV_MODE", "false")
            buildConfigField("boolean", "STAGING_MODE", "false")
            resValue("string", "app_name", "picplz")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // TODO: prod 릴리즈 키 도입 시 교체. 현재는 Firebase Distribution용으로 debug 키 사이닝
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
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
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// network_security_config.xml 생성 (apiHost에서 파생 — Single Source of Truth)
abstract class GenerateNetSecConfigTask : DefaultTask() {
    @get:Input
    abstract val host: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val xmlDir = outputDir.get().asFile.resolve("xml")
        xmlDir.mkdirs()
        xmlDir.resolve("network_security_config.xml").writeText(
            """<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">${host.get()}</domain>
    </domain-config>
</network-security-config>
""",
        )
    }
}

androidComponents {
    onVariants { variant ->
        val taskName =
            "generateNetSecConfig${variant.name.replaceFirstChar { it.uppercase() }}"
        val taskProvider =
            tasks.register<GenerateNetSecConfigTask>(taskName) {
                host.set(apiHost)
                outputDir.set(layout.buildDirectory.dir("generated/res/netSecConfig/${variant.name}"))
            }
        variant.sources.res?.addGeneratedSourceDirectory(
            taskProvider,
            GenerateNetSecConfigTask::outputDir,
        )
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:chat"))
    implementation(project(":feature:photographer"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:feed"))
    implementation(project(":feature:main"))
    implementation(project(":feature:reservation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger - Hilt dependencies
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.ui.v151)
    implementation(libs.androidx.material3.v110)
    implementation(libs.ui.tooling.preview)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose.v110)

    // Hilt Testing dependencies
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // coil
    implementation(libs.coil.compose)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // kakao login
    implementation(libs.kakao.user)

    // kakao map
    implementation(libs.kakao.maps)

    // Material
    // implementation("androidx.compose.material:material:1.5.1")
}
