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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
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
 * @param itemPadding Padding to apply around each individual image item. Defaults to `4.dp`.
 * @param itemArrangement Arrangement of the individual image items. Defaults to `spacedBy(0.dp)`.
 * @param itemShape The [Shape] used to clip each individual image item.
 *            Defaults to `RoundedCornerShape(8.dp)`.
 * @param itemContentScale The [ContentScale] to apply to the images.
 *            Defaults to `ContentScale.Crop`.
 * @param itemBackgroundColor The background color to apply to the images.
 *            Defaults to `Color.Gray`.
 * @param itemBorderStroke The [BorderStroke] to apply to the images.
 *            Defaults to `BorderStroke(0.dp, Color.Transparent)`.
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
    itemPadding: Dp = 4.dp,
    itemArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
    itemShape: Shape = RoundedCornerShape(8.dp),
    itemContentScale: ContentScale = ContentScale.Crop,
    itemBackgroundColor: Color = Color.Gray,
    itemBorderStroke: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    onItemClick: (index: Int) -> Unit = {}
) {
    if (images.isEmpty()) return

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

    LazyRow(
        modifier = modifier,
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
                Box(modifier = Modifier.fillMaxHeight()) {
                    CarouselItem(
                        bitmap = row[0],
                        contentDescription = imageContentDescription(
                            startIndexForThisChunk,
                            row[0]
                        ),
                        modifier = Modifier.fillMaxHeight(),
                        padding = itemPadding,
                        shape = itemShape,
                        contentScale = itemContentScale,
                        backgroundColor = itemBackgroundColor,
                        borderStroke = itemBorderStroke
                    ) { onItemClick(startIndexForThisChunk) }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(IntrinsicSize.Min)
                ) {
                    row.forEachIndexed { itemInRowIndex, bitmap ->
                        val originalIndex = startIndexForThisChunk + itemInRowIndex
                        CarouselItem(
                            bitmap = bitmap,
                            contentDescription = imageContentDescription(originalIndex, bitmap),
                            modifier = Modifier.weight(1f),
                            padding = itemPadding,
                            shape = itemShape,
                            contentScale = itemContentScale,
                            backgroundColor = itemBackgroundColor,
                            borderStroke = itemBorderStroke
                        ) { onItemClick(originalIndex) }
                    }
                }
            }
        }
    }
}

@Composable
internal fun CarouselItem(
    modifier: Modifier,
    bitmap: Bitmap,
    contentDescription: String?,
    padding: Dp,
    shape: Shape,
    contentScale: ContentScale,
    backgroundColor: Color,
    borderStroke: BorderStroke,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(padding)
            .clip(shape)
            .background(backgroundColor)
            .border(border = borderStroke, shape = shape)
            .clickable { onClick() }
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}
