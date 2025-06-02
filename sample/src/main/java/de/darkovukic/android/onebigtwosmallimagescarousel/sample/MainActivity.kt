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
package de.darkovukic.android.onebigtwosmallimagescarousel.sample

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.OBTSICarousel
import de.darkovukic.android.onebigtwosmallimagescarousel.OBTSICarouselVisibility
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
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.this_app_demonstrates_the),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "OneBigTwoSmallImagesCarousel",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.library),
                style = MaterialTheme.typography.titleMedium
            )
        }

        val images = List(10) {
            BitmapHelpers.generateSampleBitmap(
                width = 200,
                height = 100,
                index = it
            )
        }
        val carouselHeight = 150.dp
        val onItemClick = { index: Int ->
            showClickedOnMessage(
                context = context,
                itemNumber = index + 1
            )
        }

        UsageExampleHolder(
            itemModifier = Modifier,
            carouselHeight = carouselHeight,
            images = images,
            title = stringResource(R.string.example_1_no_special_styling_specified),
            onItemClick = onItemClick
        )

        UsageExampleHolder(
            itemModifier = Modifier
                .padding(all = 4.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.Blue.copy(alpha = 0.2f)),
            carouselHeight = carouselHeight,
            images = images,
            title = stringResource(R.string.example_2_google_maps_style_without_click_listener),
        )

        val shape = RoundedCornerShape(topStartPercent = 50, bottomEndPercent = 50)
        UsageExampleHolder(
            itemModifier = Modifier
                .padding(vertical = 4.dp, horizontal = 6.dp)
                .clip(shape = shape)
                .border(
                    shape = shape,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                )
                .background(color = Color.Blue.copy(alpha = 0.2f)),
            carouselHeight = carouselHeight,
            images = images,
            title = stringResource(R.string.example_3_custom_shape_and_border),
            onItemClick = onItemClick
        )
    }
}

private fun showClickedOnMessage(context: Context, itemNumber: Int) {
    Toast.makeText(
        context,
        context.getString(
            R.string.clicked_on_image,
            itemNumber.toString()
        ),
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun UsageExampleHolder(
    itemModifier: Modifier?,
    carouselHeight: Dp,
    title: String,
    onItemClick: ((index: Int) -> Unit)? = null,
    images: List<Bitmap>
) {
    var currentVisibility by remember { mutableStateOf<OBTSICarouselVisibility?>(null) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = title,
            textAlign = TextAlign.Start
        )
        OBTSICarousel(
            modifier = Modifier.height(carouselHeight),
            images = images,
            imageContentDescription = { index, _ -> "Image at index: $index" },
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            itemModifier = itemModifier ?: Modifier,
            onScrollVisibilityChanged = { visibility ->
                currentVisibility = visibility
            },
            onItemClick = onItemClick
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp),
            color = MaterialTheme.colorScheme.tertiary,
            text = when (currentVisibility) {
                OBTSICarouselVisibility.START_VISIBLE -> stringResource(R.string.swipe_left_to_see_more)
                OBTSICarouselVisibility.MIDDLE_VISIBLE -> stringResource(R.string.swipe_left_or_right_to_see_more)
                OBTSICarouselVisibility.END_VISIBLE -> stringResource(R.string.swipe_right_to_see_more)
                OBTSICarouselVisibility.ALL_VISIBLE -> stringResource(R.string.complete_carousel_visible)
                else -> ""
            },
            textAlign = TextAlign.Center
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
fun ContentPreview() {
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
