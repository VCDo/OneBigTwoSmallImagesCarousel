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

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

/**
 * A Jetpack Compose Composable that displays a horizontally scrollable carousel of images
 * in a "one big, two small" repeating pattern.
 *
 * The carousel arranges images by showing one large image, followed by a column of two smaller images,
 * and repeats this pattern. It takes a list of [Bitmap] objects as input.
 *
 * Users can customize padding, item spacing, shape, and borders for the images.
 * An optional click listener provides the index of the clicked image from the original list.

 * @param modifier The [Modifier] to be applied to the entire carousel layout.
 * @param images The list of [Bitmap] objects to display in the carousel.
 * @param imageContentDescription A lambda that provides a content description for each image.
 * @param contentPadding [PaddingValues] to apply around the content of the underlying `LazyRow`.
 *            Defaults to `PaddingValues(all = 12.dp)`.
 * @param itemModifier The [Modifier] to be applied to each individual carousel item's container (`Box`).
 *            This allows for full customization of padding, shape, background, border, etc., for each item.
 *            Essential layout modifiers like `aspectRatio` and `clickable` are applied by the component itself
 *            before this user-provided modifier. Defaults to `Modifier`.
 * @param itemArrangement Arrangement of the individual image items. Defaults to `spacedBy(0.dp)`.
 * @param itemContentScale The [ContentScale] to apply to the images.
 *            Defaults to `ContentScale.Crop`.
 * @param onScrollVisibilityChanged A lambda that is invoked when the visibility state of the
 *            carousel changes.
 * @param onItemClick A lambda that is invoked when an image in the carousel is clicked.
 *            It receives the index of the clicked image from the original [images] list.
 *            Defaults to an empty lambda.
 */
@Composable
fun OBTSICarousel(
    modifier: Modifier = Modifier,
    images: List<Bitmap>,
    imageContentDescription: (index: Int, bitmap: Bitmap) -> String?,
    contentPadding: PaddingValues = PaddingValues(all = 12.dp),
    itemModifier: Modifier = Modifier
        .padding(4.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color.Gray)
        .border(BorderStroke(0.dp, Color.Transparent), RoundedCornerShape(8.dp)),
    itemArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
    itemContentScale: ContentScale = ContentScale.Crop,
    onScrollVisibilityChanged: (CarouselVisibility) -> Unit = {},
    onItemClick: (index: Int) -> Unit = {}
) {
    if (images.isEmpty()) {
        // When the list is empty, report ALL_VISIBLE and return.
        // This ensures the callback is triggered even for an empty state.
        LaunchedEffect(Unit) {
            onScrollVisibilityChanged(CarouselVisibility.ALL_VISIBLE)
        }
        return
    }

    val chunkedBitmaps = remember(images) {
        val chunkList = mutableListOf<List<Bitmap>>()
        var chunkSize = 1
        var index = 0
        while (index < images.size) {
            val row = images.subList(
                fromIndex = index,
                toIndex = minOf(index + chunkSize, images.size)
            )
            chunkList.add(row)
            index += row.size
            chunkSize = if (chunkSize == 1) 2 else 1
        }
        chunkList
    }

    val lazyListState = rememberLazyListState()

    // Use the observer to handle visibility logic
    CarouselVisibilityObserver(
        lazyListState = lazyListState,
        totalItemCount = chunkedBitmaps.size,
        onScrollVisibilityChanged = onScrollVisibilityChanged
    )

    LazyRow(
        modifier = modifier,
        state = lazyListState,
        contentPadding = contentPadding,
        horizontalArrangement = itemArrangement
    ) {
        itemsIndexed(chunkedBitmaps) { chunkIndex, row ->
            // Calculate the start index for this chunk
            val startIndexForThisChunk = remember(chunkIndex, chunkedBitmaps) {
                var calculatedStartIndex = 0
                for (i in 0 until chunkIndex) {
                    calculatedStartIndex += chunkedBitmaps[i].size
                }
                calculatedStartIndex
            }

            if (row.size == 1) {
                // Base modifiers essential for the big item's layout and functionality
                val baseModifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable { onItemClick(startIndexForThisChunk) }

                CarouselItem(
                    // Apply base modifiers first, then the user-provided modifier
                    modifier = baseModifier.then(itemModifier),
                    bitmap = row[0],
                    contentDescription = imageContentDescription(
                        startIndexForThisChunk,
                        row[0]
                    ),
                    contentScale = itemContentScale
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(IntrinsicSize.Min)
                ) {
                    row.forEachIndexed { itemInRowIndex, bitmap ->
                        val originalIndex = startIndexForThisChunk + itemInRowIndex
                        // Base modifiers essential for the small item's layout and functionality
                        val baseModifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clickable { onItemClick(originalIndex) }

                        CarouselItem(
                            // Apply base modifiers first, then the user-provided modifier
                            modifier = baseModifier.then(itemModifier),
                            bitmap = bitmap,
                            contentDescription = imageContentDescription(originalIndex, bitmap),
                            contentScale = itemContentScale
                        )
                    }
                }
            }
        }
    }
}

/**
 * Internal composable representing a single item in the carousel.
 * It displays an image within a Box that is styled by the provided modifier.
 *
 * @param modifier The [Modifier] to be applied to the item's container (`Box`).
 *                 This modifier is constructed in [OBTSICarousel] and includes
 *                 base layout modifiers, click handling, and user-customizations.
 * @param bitmap The [Bitmap] to display.
 * @param contentDescription The content description for the image.
 * @param contentScale The [ContentScale] for the image.
 */
@Composable
internal fun CarouselItem(
    modifier: Modifier,
    bitmap: Bitmap,
    contentDescription: String?,
    contentScale: ContentScale
) {
    Box(
        modifier = modifier
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}
