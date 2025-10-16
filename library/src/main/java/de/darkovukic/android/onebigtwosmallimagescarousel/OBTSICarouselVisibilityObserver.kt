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

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

/**
 * Defines the different visibility states of the carousel as it's scrolled.
 * This is reported by [CarouselVisibilityObserver].
 */
enum class OBTSICarouselVisibility {
    /**
     * Indicates that the start of the carousel (the first item) is fully visible,
     * but the end of the carousel is not.
     */
    START_VISIBLE,

    /**
     * Indicates that neither the start nor the end of the carousel is fully visible.
     * This state typically occurs when scrolling through the middle portion of the carousel.
     */
    MIDDLE_VISIBLE,

    /**
     * Indicates that the end of the carousel (the last item) is fully visible,
     * but the start of the carousel is not.
     */
    END_VISIBLE,

    /**
     * Indicates that the entire carousel, from the first item to the last item,
     * is fully visible within the viewport. This can happen if there are few items
     * or if the viewport is wide enough to contain all items. It also applies
     * if the carousel is empty.
     */
    ALL_VISIBLE
}

/**
 * Observes the scroll state of a LazyList and reports its visibility status.
 * This is a non-UI composable, meaning it doesn't render anything itself,
 * but uses Composition an LaunchedEffects to manage state and callbacks.
 *
 * @param lazyListState The [LazyListState] of the list to observe.
 * @param totalItemCount The total number of items in the list (chunks in this case).
 * @param onScrollVisibilityChanged Callback invoked when the visibility state changes.
 */
@Composable
internal fun CarouselVisibilityObserver(
    lazyListState: LazyListState,
    totalItemCount: Int,
    onScrollVisibilityChanged: (OBTSICarouselVisibility) -> Unit
) {
    // This derived state will automatically re-evaluate whenever the scroll state or item count changes.
    val visibility: OBTSICarouselVisibility? by remember(lazyListState, totalItemCount) {
        derivedStateOf {
            // If there are no items, the carousel is considered fully visible.
            if (totalItemCount == 0) {
                return@derivedStateOf OBTSICarouselVisibility.ALL_VISIBLE
            }

            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            // If no items are currently visible in the layout, we can't determine the state.
            if (visibleItemsInfo.isEmpty()) {
                return@derivedStateOf null
            }

            val firstVisibleItem = visibleItemsInfo.first()
            val lastVisibleItem = visibleItemsInfo.last()

            // Check if the very first item (index 0) is fully visible at the start of the viewport.
            val firstItemFullyVisible = firstVisibleItem.index == 0
                    && firstVisibleItem.offset >= layoutInfo.viewportStartOffset
            // Check if the very last item is fully visible at the end of the viewport.
            val lastItemFullyVisible = lastVisibleItem.index == totalItemCount - 1
                    && (lastVisibleItem.offset + lastVisibleItem.size) <= layoutInfo.viewportEndOffset
            // Check if all items in the list are currently rendered in the viewport.
            val allItemsVisibleInLayout = visibleItemsInfo.size == totalItemCount

            // Determine the overall visibility state based on the conditions above.
            when {
                // If all items are rendered and both the first and last are fully visible, the whole list is visible.
                allItemsVisibleInLayout && firstItemFullyVisible && lastItemFullyVisible -> OBTSICarouselVisibility.ALL_VISIBLE
                // If the first item is fully visible but the last one isn't, we're at the start.
                firstItemFullyVisible && !lastItemFullyVisible -> OBTSICarouselVisibility.START_VISIBLE
                // If the last item is fully visible but the first one isn't, we're at the end.
                lastItemFullyVisible && !firstItemFullyVisible -> OBTSICarouselVisibility.END_VISIBLE
                // If all items are rendered in the layout (for short lists), it's considered fully visible.
                allItemsVisibleInLayout -> OBTSICarouselVisibility.ALL_VISIBLE
                // Otherwise, we must be scrolling somewhere in the middle.
                else -> OBTSICarouselVisibility.MIDDLE_VISIBLE
            }
        }
    }

    // This effect runs whenever the calculated `visibility` state changes.
    LaunchedEffect(visibility) {
        // When the visibility is not null, invoke the callback to notify the caller.
        visibility?.let { onScrollVisibilityChanged(it) }
    }
}
