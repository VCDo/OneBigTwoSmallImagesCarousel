# OneBigTwoSmallImagesCarousel
[![](https://jitpack.io/v/VCDo/OneBigTwoSmallImagesCarousel.svg)](https://jitpack.io/#VCDo/OneBigTwoSmallImagesCarousel)

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

## Usage

Here's a basic example of how to use `OBTSICarousel` in your Composable function. First, make sure
you have a list of `Bitmap` objects ready.
```kotlin
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Import OBTSICarousel Composable
import de.darkovukic.android.onebigtwosmallimagescarousel.OBTSICarousel
// Import BitmapHelpers for generating sample Bitmaps
import de.darkovukic.android.onebigtwosmallimagescarousel.util.BitmapHelpers

@Composable
fun MyScreenWithCarousel() {
    // 1. Prepare your list of Bitmaps
    // In a real application, you would load these from your ViewModel,
    // local storage, network, etc.
    val imageBitmaps: List<Bitmap> = remember {
        // Example with 9 images
        List(9) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 100, index = it)
        }
    }

    if (imageBitmaps.isEmpty()) {
        // Handle empty state if necessary
        // Text("No images to display.")
        return
    }

    // 2. Use the OBTSICarousel Composable
    OBTSICarousel(
        // >>> Required:

        // Set a height for the carousel
        modifier = Modifier.height(300.dp),
        
        // The list of images to display
        images = imageBitmaps,
        
        // Lambda for handling image content descriptions
        imageContentDescription = { index, _ -> "Image at index: $index" },
        
        // Callback when an image is clicked
        onItemClick = {
            // Handle item click, 'it' is the index in your original 'bitmaps' list
        },
        
        // >>> Optional Styling:

        // Padding around the content
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),

        // Padding around each individual image
        itemPadding = 6.dp,

        // Shape for clipping each image
        itemShape = RoundedCornerShape(12.dp)
    )
}
```

### Parameters

*   `modifier`: (Optional) Standard Jetpack Compose `Modifier` to be applied to the root of the `OBTSICarousel`. Defaults to `Modifier`.
*   `images`: A `List<Bitmap>` of images to display in the carousel. This is a required parameter.
*   `imageContentDescription`: A lambda that provides a content description for each image.
*   `contentPadding`: (Optional) `PaddingValues` for the internal `LazyRow` that holds the carousel items. Defaults to `PaddingValues(all = 12.dp)`.
*   `itemPadding`: (Optional) `Dp` value for padding applied around each individual image within its designated space. Defaults to `4.dp`.
*   `itemShape`: (Optional) `Shape` used to clip each individual image. Defaults to `RoundedCornerShape(8.dp)`.
*   `onItemClick`: (Optional) Lambda `(index: Int) -> Unit` that is invoked when an image in the carousel is clicked. The `index` corresponds to the position in the original `bitmaps` list. Defaults to an empty lambda.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
