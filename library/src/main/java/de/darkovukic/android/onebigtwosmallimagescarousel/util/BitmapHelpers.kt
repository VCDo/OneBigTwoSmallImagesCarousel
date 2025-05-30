package de.darkovukic.android.onebigtwosmallimagescarousel.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.createBitmap

object BitmapHelpers {
    fun generateSampleBitmap(
        width: Int,
        height: Int,
        index: Int
    ): Bitmap {
        val bitmap = createBitmap(
            width = width,
            height = height
        )
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.rgb(
                (index * 40) % 255,
                (index * 80) % 255,
                (index * 120) % 255
            )
            style = Paint.Style.FILL
        }
        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(), paint
        )
        val paintText = Paint().apply {
            color = Color.WHITE
            textSize = 30f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText(
            (index + 1).toString(),
            width / 2f,
            (height / 2) - ((paintText.descent() + paintText.ascent()) / 2f),
            paintText
        )
        return bitmap
    }
}
