# OneBigTwoSmallImagesCarousel

**⚠️ Warning: This project is currently in an early stage of development and is not yet ready for
production use or as a stable library. ⚠️**

## Overview

**OneBigTwoSmallImagesCarousel** is an Android **Jetpack Compose** library that provides a
customizable image carousel that displays one large image followed by two small images in a
repeating pattern:
- 1 large image
- 2 small stacked images
- 1 large image
- 2 small stacked images
- …

## Features

- Built entirely with Jetpack Compose
- Horizontally scrollable layout
- Customizable base layout thru Modifier
- Customizable image padding and shape
- Designed for any number of images (automatically batches them in the pattern: 1 large + 2 small)

## Installation

The library is available via [JitPack](https://jitpack.io).

### Step 1 – Add JitPack to your root `settings.gradle`

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
### Step 2 – Add the dependency to your module's `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.VCDo:OneBigTwoSmallImagesCarousel:<latest-version>")
}
```

Replace `<latest-version>` with the latest release tag (e.g., `1.0.0`).
(You will then find the current releases on the [Releases page](https://github.com/VCDo/OneBigTwoSmallImagesCarousel/releases) of this repository.)

For local testing, you can include the `:library` module directly as a project dependency in your
sample app: `implementation(project(":library"))`.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
