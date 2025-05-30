package de.darkovukic.android.onebigtwosmallimagescarousel

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.util.BitmapHelpers

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
 * @param bitmaps The list of [Bitmap] objects to display in the carousel.
 * @param contentPadding [PaddingValues] to apply around the content of the underlying `LazyRow`.
 *            Defaults to `PaddingValues(all = 12.dp)`.
 * @param itemPadding Padding to apply around each individual image item. Defaults to `4.dp`.
 * @param itemShape The [Shape] used to clip each individual image item.
 *            Defaults to `RoundedCornerShape(8.dp)`.
 * @param onItemClick A lambda that is invoked when an image in the carousel is clicked.
 *            It receives the index of the clicked image from the original [bitmaps] list.
 *            Defaults to an empty lambda.
 */
@Composable
fun OBTSICarousel(
    modifier: Modifier = Modifier,
    bitmaps: List<Bitmap>,
    contentPadding: PaddingValues = PaddingValues(all = 12.dp),
    itemPadding: Dp = 4.dp,
    itemShape: Shape = RoundedCornerShape(8.dp),
    onItemClick: (index: Int) -> Unit = {}
) {
    if (bitmaps.isEmpty()) return

    val chunkedBitmaps = remember(bitmaps) {
        val chunkList = mutableListOf<List<Bitmap>>()
        var chunkSize = 1
        var index = 0

        while (index < bitmaps.size) {
            val row = bitmaps.subList(
                fromIndex = index,
                toIndex = minOf(index + chunkSize, bitmaps.size)
            )
            chunkList.add(row)
            index += row.size
            chunkSize = if (chunkSize == 1) 2 else 1
        }

        chunkList
    }

    var currentBitmapIndex = 0
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(chunkedBitmaps) { row ->
            if (row.size == 1) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    val originalIndex = currentBitmapIndex
                    HorizontalBitmapItem(
                        bitmap = row[0],
                        modifier = Modifier.fillMaxHeight(),
                        padding = itemPadding,
                        shape = itemShape
                    ) { onItemClick(originalIndex) }
                }
                currentBitmapIndex += 1
            } else {
                Column(modifier = Modifier.fillMaxHeight()) {
                    row.forEachIndexed { itemInRowIndex, bitmap ->
                        val originalIndex = currentBitmapIndex + itemInRowIndex
                        HorizontalBitmapItem(
                            bitmap = bitmap,
                            modifier = Modifier.weight(0.5f),
                            padding = itemPadding,
                            shape = itemShape
                        ) { onItemClick(originalIndex) }
                    }
                }
                currentBitmapIndex += 2
            }
        }
    }
}

@Composable
fun HorizontalBitmapItem(
    bitmap: Bitmap,
    modifier: Modifier,
    padding: Dp,
    shape: Shape,
    onClick: (Bitmap) -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(padding)
            .clip(shape)
            .clickable { onClick(bitmap) }
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    device = Devices.PIXEL_7_PRO,
    apiLevel = 34
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    device = Devices.PIXEL_7_PRO,
    apiLevel = 34
)
@Composable
fun OBTSICarouselPreview() {
    OBTSICarousel(
        modifier = Modifier.height(300.dp),
        bitmaps = List(4) {
            BitmapHelpers.generateSampleBitmap(
                width = 200,
                height = 100,
                index = it
            )
        }
    )
}
