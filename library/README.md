# OneBigTwoSmallImagesCarousel

**⚠️ Warning: This project is currently in an early stage of development and is not yet ready for
production use or as a stable library. ⚠️**

## Overview

OneBigTwoSmallImagesCarousel is an Android Jetpack Compose library that provides a customizable
image carousel that displays one large image followed by two small images in a repeating pattern.

## Features

* Display images in a repeating pattern of one large image followed by two small images.
* Fully customizable with Jetpack Compose.
* Supports loading images from local resources or remote URLs.

## Installation (Future)

Once the library is more stable and published, the installation will likely look like this:

1.  **Add JitPack to your root `build.gradle.kts` (or `settings.gradle.kts`):**
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```
2.  **Add the dependency to your module's `build.gradle.kts`:**
```kotlin
dependencies {
    implementation("com.github.VCDo:OneBigTwoSmallImagesCarousel:VERSION_HERE") // Replace VERSION_HERE with the actual version tag (e.g., v1.0.0)
}
```
(You will then find the current releases on the [Releases page](https://github.com/VCDo/OneBigTwoSmallImagesCarousel/releases) of this repository.)

**Note:** As the project is not yet published, this step is currently not applicable. 
For local testing, you can include the `:library` module directly as a project dependency in your
sample app: `implementation(project(":library"))`.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details
(you would need to add a `LICENSE` file with the MIT License text).
