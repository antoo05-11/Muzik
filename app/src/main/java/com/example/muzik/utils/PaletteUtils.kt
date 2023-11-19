package com.example.muzik.utils

import android.content.res.Resources
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

    fun getDominantGradient(
        bitmap: Bitmap,
        cornerRadius: Float? = null,
        orientation: GradientDrawable.Orientation? = null,
        endColor: String? = null
    ): GradientDrawable {
        val topColor = getUpperSideDominantColor(bitmap)
        var topHex = String.format("#%06X", 0xFFFFFF and topColor)

        val y = 0.2126 * (hexToRgb(topHex)?.first ?: 0)
        +0.7152 * (hexToRgb(topHex)?.second ?: 0) +
                0.0722 * (hexToRgb(topHex)?.third ?: 0)


        var endHex = endColor?.let { String.format("#%06X", 0xFFFFFF and Color.parseColor(it)) }
            ?: String.format("#%06X", 0xFFFFFF and getLowerSideDominantColor(bitmap))

        if (y > 40) {
            topHex = String.format("#%06X", 0xFFFFFF and Color.DKGRAY)
            if (endColor == null) {
                endHex = String.format("#%06X", 0xFFFFFF and Color.DKGRAY)
            }
        }

        val colors = intArrayOf(Color.parseColor(topHex), Color.parseColor(endHex))
        val actualOrientation = orientation ?: GradientDrawable.Orientation.TOP_BOTTOM

        val gradientDrawable = GradientDrawable(actualOrientation, colors)

        cornerRadius?.let {
            gradientDrawable.cornerRadius = convertDpToPixels(it).toFloat()
        }

        return gradientDrawable
    }

}

fun convertDpToPixels(dp: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun hexToRgb(hex: String): Triple<Int, Int, Int>? {
    if (hex.length != 7 || hex[0] != '#') {
        return null
    }

    return try {
        val r = hex.substring(1, 3).toInt(16)
        val g = hex.substring(3, 5).toInt(16)
        val b = hex.substring(5, 7).toInt(16)
        Triple(r, g, b)
    } catch (e: NumberFormatException) {
        null
    }
}
