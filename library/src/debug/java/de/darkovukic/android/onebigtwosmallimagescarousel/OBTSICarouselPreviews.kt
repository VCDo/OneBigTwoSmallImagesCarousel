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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.util.BitmapHelpers

@Preview(name = "Only required parameters specified")
@Composable
fun OBTSICarouselPreviewDefault() {
    OBTSICarousel(
        modifier = Modifier.height(150.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(
                width = 200,
                height = 100,
                index = it
            )
        },
        imageContentDescription = { index, _ -> "Image at index: $index" }
    )
}

@Preview(name = "All parameter specified")
@Composable
fun OBTSICarouselPreviewAll() {
    val shape = RoundedCornerShape(16.dp)
    OBTSICarousel(
        modifier = Modifier.height(150.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(
                width = 200,
                height = 100,
                index = it
            )
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
        modifier = Modifier.height(150.dp),
        images = List(6) {
            BitmapHelpers.generateSampleBitmap(
                width = 200,
                height = 100,
                index = it
            )
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
