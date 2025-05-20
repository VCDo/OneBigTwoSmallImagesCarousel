package de.darkovukic.android.onebigtwosmallimagescarousel.sample

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.OBTSICarousel
import de.darkovukic.android.onebigtwosmallimagescarousel.sample.ui.theme.OneBigTwoSmallImagesCarouselTheme
import de.darkovukic.android.onebigtwosmallimagescarousel.util.BitmapHelpers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OneBigTwoSmallImagesCarouselTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Content(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.this_project_demonstrates),
            textAlign = TextAlign.Center
        )
        OBTSICarousel(
            modifier = Modifier.height(300.dp),
            bitmaps = List(9) {
                BitmapHelpers.generateSampleBitmap(
                    width = 200,
                    height = 100,
                    index = it
                )
            }
        )
        Text(
            text = stringResource(R.string.swipe_left_to_see_more),
            modifier = Modifier.padding(16.dp)
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
fun GreetingPreview() {
    OneBigTwoSmallImagesCarouselTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Content(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}
