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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


/**
 * Defines the different visibility states of the carousel as it's scrolled.
 * This is reported by [CarouselVisibilityObserver].
 */
enum class CarouselVisibility {
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
    onScrollVisibilityChanged: (CarouselVisibility) -> Unit
) {
    var currentVisibilityState by remember { mutableStateOf(CarouselVisibility.ALL_VISIBLE) }

    val visibility by remember(totalItemCount) { // Depends on the number of items
        derivedStateOf {
            if (totalItemCount == 0) {
                CarouselVisibility.ALL_VISIBLE
            } else {
                val layoutInfo = lazyListState.layoutInfo
                val visibleItemsInfo = layoutInfo.visibleItemsInfo

                if (visibleItemsInfo.isEmpty()) {
                    // If items exist (totalItemCount > 0) but none are visible yet (e.g., during initial layout)
                    // It's often safer to maintain the current state or assume a default like START if scrollable.
                    // Relying on derivedState to eventually stabilize is also an option.
                    // For now, let's keep the current state, as it might briefly be empty during fast scrolls.
                    // However, for an *initial* state, if totalItemCount > 0 and visibleItemsInfo is empty,
                    // it's likely START_VISIBLE if scrollable, or ALL_VISIBLE if not (though this is complex to determine here).
                    // Let's assume for the initial LaunchedEffect, if items exist but none are visible,
                    // the derivedState will soon pick up the correct state.
                    // For the *very first* emission if empty but should not be, we might want a default.
                    // Let's refine this: if totalItemCount > 0 and visibleItemsInfo is empty, it could mean layout is not ready.
                    // The derivedStateOf should handle this.
                    // For this initial effect, we might default to what derivedStateOf would likely compute.
                    // However, to be safe and avoid complex initial guesses, let's keep currentVisibilityState.
                    // The derivedState will trigger the correct update soon.
                    currentVisibilityState // Or perhaps calculate based on logic below if we assume layout is stable enough
                } else {
                    // visibleItemsInfo is NOT empty, and totalItemCount > 0
                    val firstVisibleItem =
                        visibleItemsInfo.first() // No need for firstOrNull, as it's not empty
                    val lastVisibleItem = visibleItemsInfo.last()  // No need for lastOrNull

                    val firstItemFullyVisible =
                        firstVisibleItem.index == 0 && firstVisibleItem.offset >= layoutInfo.viewportStartOffset
                    val lastItemFullyVisible =
                        lastVisibleItem.index == totalItemCount - 1 && (lastVisibleItem.offset + lastVisibleItem.size) <= layoutInfo.viewportEndOffset
                    val allItemsVisibleInLayout =
                        visibleItemsInfo.size == totalItemCount // No need to check first/last index here, size is enough if they are contiguous

                    if (allItemsVisibleInLayout && firstItemFullyVisible && lastItemFullyVisible) {
                        CarouselVisibility.ALL_VISIBLE
                    } else if (firstItemFullyVisible && !lastItemFullyVisible) {
                        CarouselVisibility.START_VISIBLE
                    } else if (lastItemFullyVisible && !firstItemFullyVisible) {
                        CarouselVisibility.END_VISIBLE
                    } else if (allItemsVisibleInLayout) { // All items fit, but might be slightly scrolled (e.g., during snap)
                        CarouselVisibility.ALL_VISIBLE
                    } else {
                        CarouselVisibility.MIDDLE_VISIBLE
                    }
                }
            }
        }
    }

    // Effect to call the callback when visibility changes
    LaunchedEffect(visibility) {
        if (currentVisibilityState != visibility) {
            currentVisibilityState = visibility
            onScrollVisibilityChanged(visibility)
        }
    }

    LaunchedEffect(
        Unit,
        totalItemCount,
        lazyListState
    ) { // Add lazyListState to re-evaluate if it changes externally
        val newVisibility = if (totalItemCount == 0) {
            CarouselVisibility.ALL_VISIBLE
        } else {
            // At this point, totalItemCount > 0 is guaranteed.
            val layoutInfo = lazyListState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                // If items exist (totalItemCount > 0) but none are visible yet (e.g., during initial layout phase).
                // It's tricky to determine the exact state before the first proper layout pass and derivedStateOf calculation.
                // Maintaining currentVisibilityState or using a sensible default (like START_VISIBLE if scrollable) are options.
                // For robustness in this initial effect, let's keep currentVisibilityState.
                // The derivedStateOf and subsequent LaunchedEffect(visibility) should soon provide the accurate state.
                currentVisibilityState
            } else {
                // visibleItemsInfo is NOT empty, and totalItemCount > 0.
                // Re-evaluate the core visibility logic for the initial state.
                val firstVisibleItem = visibleItemsInfo.first() // No need for firstOrNull
                val lastVisibleItem = visibleItemsInfo.last()  // No need for firstOrNull

                val firstItemFullyVisible =
                    firstVisibleItem.index == 0 && firstVisibleItem.offset >= layoutInfo.viewportStartOffset
                val lastItemFullyVisible =
                    lastVisibleItem.index == totalItemCount - 1 && (lastVisibleItem.offset + lastVisibleItem.size) <= layoutInfo.viewportEndOffset
                val allItemsVisibleInLayout = visibleItemsInfo.size == totalItemCount

                if (allItemsVisibleInLayout && firstItemFullyVisible && lastItemFullyVisible) {
                    CarouselVisibility.ALL_VISIBLE
                } else if (firstItemFullyVisible && !lastItemFullyVisible) {
                    CarouselVisibility.START_VISIBLE
                } else if (lastItemFullyVisible && !firstItemFullyVisible) {
                    CarouselVisibility.END_VISIBLE
                } else if (allItemsVisibleInLayout) { // All items fit, but might be slightly scrolled (e.g., during snap)
                    CarouselVisibility.ALL_VISIBLE
                } else {
                    CarouselVisibility.MIDDLE_VISIBLE
                }
            }
        }

        // Only update and call callback if the state has actually changed from its initial value or last set value
        if (currentVisibilityState != newVisibility) {
            currentVisibilityState = newVisibility
            onScrollVisibilityChanged(newVisibility)
        }
    }
}
