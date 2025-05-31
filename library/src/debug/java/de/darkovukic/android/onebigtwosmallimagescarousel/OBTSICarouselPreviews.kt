/*
 * Copyright (c) 2025 VCDo (Darko Vukic)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.darkovukic.android.onebigtwosmallimagescarousel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.util.BitmapHelpers

@Preview(name = "Only required parameters specified")
@Composable
fun OBTSICarouselPreviewDefault() {
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 100, index = it)
        },
        imageContentDescription = { index, _ -> "Image at index: $index" }
    )
}

@Preview(name = "All parameter specified")
@Composable
fun OBTSICarouselPreviewAll() {
    val shape = RoundedCornerShape(16.dp)
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 100, index = it)
        },
        imageContentDescription = { index, _ -> "Image at index: $index" },
        contentPadding = PaddingValues(6.dp),
        itemModifier = Modifier
            .padding(8.dp)
            .clip(shape)
            .background(Color.DarkGray)
            .border(BorderStroke(1.dp, Color.Gray), shape),
        itemArrangement = Arrangement.spacedBy(8.dp),
        itemContentScale = ContentScale.Inside,
        onScrollVisibilityChanged = {},
        onItemClick = {}
    )
}

@Preview(name = "No padding")
@Composable
fun OBTSICarouselPreviewNoPadding() {
    val shape = CutCornerShape(0.dp)
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 100, index = it)
        },
        imageContentDescription = { index, _ -> "Image at index: $index" },
        contentPadding = PaddingValues(0.dp),
        itemModifier = Modifier
            .padding(0.dp)
            .clip(shape)
            .background(Color.DarkGray)
            .border(BorderStroke(1.dp, Color.DarkGray), shape)
    )
}

@Preview(name = "Custom Padding, Shape & Background")
@Composable
fun OBTSICarouselPreviewStyledItems() {
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 120, index = it)
        },
        imageContentDescription = { index, _ -> "Styled image $index" },
        itemModifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp) // Asymmetric padding
            .clip(CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)) // Interesting shape
            .background(Color.Cyan.copy(alpha = 0.3f))
    )
}

@Preview(name = "Individual Borders & Inner Padding")
@Composable
fun OBTSICarouselPreviewBordersAndInnerPadding() {
    val customShape = RoundedCornerShape(8.dp)
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(5) {
            BitmapHelpers.generateSampleBitmap(width = 180, height = 100, index = it)
        },
        imageContentDescription = { index, _ -> "Bordered image $index" },
        itemContentScale = ContentScale.Fit, // Change content scale for variety
        itemModifier = Modifier
            .padding(4.dp) // Outer spacing for items
            .border(BorderStroke(2.dp, Color.Gray), customShape)
            .padding(6.dp) // Inner padding, inside the border
            .clip(customShape) // Clip content inside the inner padding
            .background(Color.DarkGray)
    )
}

@Preview(name = "Shadow Elevation (Conceptual)")
@Composable
fun OBTSICarouselPreviewShadowItems() {
    // Note: Modifier.shadow() works best with a background color on the element itself.
    // The shadow is drawn around the bounds defined by the clip and size.
    OBTSICarousel(
        modifier = Modifier
            .height(120.dp)
            .padding(16.dp), // Padding around carousel for shadow visibility
        images = List(4) {
            BitmapHelpers.generateSampleBitmap(width = 220, height = 130, index = it)
        },
        imageContentDescription = { index, _ -> "Shadow image $index" },
        itemModifier = Modifier
            .padding(8.dp) // Give space for the shadow not to overlap too much
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp), clip = true)
            .background(Color.White) // Background needed for shadow to look good
            .clip(RoundedCornerShape(12.dp)) // Ensure content respects the shape
    )
}

@Preview(name = "Different Aspect Ratio (via Size in Modifier)")
@Composable
fun OBTSICarouselPreviewDifferentAspectRatioItems() {
    // WARNING: Overriding aspect ratio can break the "one big, two small" visual balance
    // if not done carefully. This preview is more to demonstrate Modifier power.
    // OBTSICarousel still applies its internal .aspectRatio(1f) first.
    // To truly change the aspect ratio of the item's content area,
    // you'd typically wrap the Image in another Box with the desired size/aspectRatio.
    // For this example, we'll size the Box, and Image will fill it.
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(3) {
            BitmapHelpers.generateSampleBitmap(width = 150, height = 150, index = it)
        },
        imageContentDescription = { index, _ -> "Sized image $index" },
        itemModifier = Modifier
            .padding(4.dp)
            .size(width = 100.dp, height = 150.dp) // Example of forcing size
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Yellow.copy(alpha = 0.5f))
    )
}

@Preview(name = "Conditional Modifier (e.g. first item special)")
@Composable
fun OBTSICarouselPreviewConditionalStyling() {
    // This example is conceptual as the modifier is the same for all items.
    // To achieve true conditional styling per item based on its index from OBTSICarousel,
    // you would need a more advanced slot API for item rendering.
    // However, we can show a complex modifier.
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 100, index = it)
        },
        imageContentDescription = { index, _ -> "Conditionally styled image $index (concept)" },
        itemArrangement = Arrangement.spacedBy(8.dp),
        itemModifier = Modifier
            .padding(vertical = 5.dp)
            .then(
                // Example of a complex, chained modifier
                if (System.currentTimeMillis() % 2 == 0L) { // Just a dummy condition for preview
                    Modifier
                        .border(BorderStroke(3.dp, Color.Green), CircleShape)
                        .padding(3.dp)
                        .clip(CircleShape)
                } else {
                    Modifier
                        .clip(RoundedCornerShape(topStartPercent = 50, bottomEndPercent = 50))
                        .background(Color.Blue.copy(alpha = 0.2f))
                }
            )
    )
}

@Preview(name = "No item styling (default Modifier)")
@Composable
fun OBTSICarouselPreviewNoItemModifier() {
    OBTSICarousel(
        modifier = Modifier.height(120.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(width = 200, height = 100, index = it)
        },
        imageContentDescription = { index, _ -> "Default item image $index" }
        // itemModifier = Modifier (using default)
    )
}
