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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import de.darkovukic.android.onebigtwosmallimagescarousel.OBTSICarouselVisibility
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
    val context = LocalContext.current
    var currentVisibility by remember { mutableStateOf<OBTSICarouselVisibility?>(null) }

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
        val shape = RoundedCornerShape(8.dp)
        OBTSICarousel(
            modifier = Modifier.height(300.dp),
            images = List(9) {
                BitmapHelpers.generateSampleBitmap(
                    width = 200,
                    height = 100,
                    index = it
                )
            },
            imageContentDescription = { index, _ -> "Image at index: $index" },
            contentPadding = PaddingValues(12.dp),
            itemModifier = Modifier
                .padding(4.dp)
                .clip(shape)
                .background(Color.LightGray)
                .border(BorderStroke(1.dp, Color.White), shape),
            onScrollVisibilityChanged = { visibility ->
                currentVisibility = visibility
            }
        ) {
            showClickedOnMessage(
                context = context,
                itemNumber = it + 1
            )
        }
        Text(
            text = when (currentVisibility) {
                OBTSICarouselVisibility.START_VISIBLE -> stringResource(R.string.swipe_left_to_see_more)
                OBTSICarouselVisibility.MIDDLE_VISIBLE -> stringResource(R.string.swipe_left_or_right_to_see_more)
                OBTSICarouselVisibility.END_VISIBLE -> stringResource(R.string.swipe_right_to_see_more)
                OBTSICarouselVisibility.ALL_VISIBLE -> stringResource(R.string.complete_carousel_visible)
                else -> ""
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
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
