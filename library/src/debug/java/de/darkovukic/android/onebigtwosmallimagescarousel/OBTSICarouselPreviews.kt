package de.darkovukic.android.onebigtwosmallimagescarousel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        itemPadding = 8.dp,
        itemArrangement = Arrangement.spacedBy(8.dp),
        itemShape = RoundedCornerShape(16.dp),
        itemContentScale = ContentScale.Inside,
        itemBackgroundColor = Color.DarkGray,
        itemBorderStroke = BorderStroke(width = 1.dp, color = Color.Gray)
    )
}

@Preview(name = "No padding")
@Composable
fun OBTSICarouselPreviewNoPadding() {
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
        itemPadding = 0.dp,
        itemShape = CutCornerShape(0.dp),
        itemBorderStroke = BorderStroke(width = 1.dp, color = Color.DarkGray)
    )
}
