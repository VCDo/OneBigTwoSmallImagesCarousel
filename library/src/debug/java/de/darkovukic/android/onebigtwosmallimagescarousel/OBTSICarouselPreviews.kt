package de.darkovukic.android.onebigtwosmallimagescarousel

import android.content.res.Configuration
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.util.BitmapHelpers

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 34
)
@Composable
fun OBTSICarouselPreviewDefault() {
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
