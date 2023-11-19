package com.example.muzik.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.palette.graphics.Palette

object PaletteUtils {
    private fun getUpperSideDominantColor(bitmap: Bitmap): Int {
        val builder = Palette.Builder(bitmap)
            .setRegion(0, 0, bitmap.width, bitmap.height / 2)
        val defaultValue = 0xFFFFFF
        val p = builder.generate()
        return p.getDominantColor(defaultValue)
    }

    private fun getLowerSideDominantColor(bitmap: Bitmap): Int {
        val defaultValue = 0xFFFFFF
        val builder = Palette.Builder(bitmap)
            .setRegion(0, bitmap.height / 2, bitmap.width, bitmap.height)
        return builder.generate().getDominantColor(defaultValue)
    }

    fun getDominantGradient(bitmap: Bitmap): GradientDrawable {
        val topColor = getUpperSideDominantColor(bitmap)
        val topHex = String.format("#%06X", 0xFFFFFF and topColor)
        val colors = intArrayOf(Color.parseColor(topHex), Color.parseColor("#303030"))
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            colors
        )
    }
}