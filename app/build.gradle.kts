plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.hilt.android)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
}

android {
	namespace = "com.wit.employeedirectory"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.wit.employeedirectory"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildFeatures {
		compose = true
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
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.fragment.ktx)
	implementation(libs.material)
	testImplementation(libs.junit)
	testImplementation(libs.kotlinx.coroutines.test)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	// Android Studio Preview support
	implementation(libs.androidx.ui.tooling.preview)
	debugImplementation(libs.androidx.ui.tooling)

	// Compose
	val composeBom = platform("androidx.compose:compose-bom:2025.02.00")
	implementation(composeBom)
	androidTestImplementation(composeBom)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.lifecycle.viewmodel.compose)

	// Glide
	implementation(libs.compose)

	// Hilt
	implementation(libs.hilt.android)
	kapt(libs.hilt.android.compiler)

	// Mockito
	testImplementation(libs.mockito.core)

	// Retrofit
	implementation(libs.converter.gson)
	implementation(libs.retrofit)
}

kapt {
	correctErrorTypes = true
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
	compilerOptions.optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
	compilerOptions.optIn.add("com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi")
	compilerOptions.optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
}