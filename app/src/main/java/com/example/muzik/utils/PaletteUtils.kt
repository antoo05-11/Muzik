package com.example.muzik.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.palette.graphics.Palette

object PaletteUtils {

    private fun getDominantColor(
        bitmap: Bitmap,
        left: Int = 0,
        top: Int = 0,
        right: Int = bitmap.width,
        bottom: Int = bitmap.height
    ): Int {
        val builder = Palette.Builder(bitmap)
            .setRegion(left, top, right, bottom)
        val defaultValue = 0xFFFFFF
        val p = builder.generate()
        return p.getDominantColor(defaultValue)
    }

    fun getDarkColor(
        bitmap: Bitmap,
        left: Int = 0,
        top: Int = 0,
        right: Int = bitmap.width,
        bottom: Int = bitmap.height
    ): Int {
        val builder = Palette.Builder(bitmap)
            .setRegion(left, top, right, bottom)
        val defaultValue = 0xFFFFFF
        val p = builder.generate()
        return p.getDarkMutedColor(defaultValue)
    }

     private fun getLightColor(
        bitmap: Bitmap,
        left: Int = 0,
        top: Int = 0,
        right: Int = bitmap.width,
        bottom: Int = bitmap.height
    ): Int {
        val builder = Palette.Builder(bitmap)
            .setRegion(left, top, right, bottom)
        val defaultValue = 0xFFFFFF
        val p = builder.generate()
        return p.getLightVibrantColor(defaultValue)
    }

    private fun getUpperSideDominantColor(bitmap: Bitmap): Int {
        return getDominantColor(bitmap, right = bitmap.width, bottom = bitmap.height / 2)
    }

    private fun getLowerSideDominantColor(bitmap: Bitmap): Int {
        return getDominantColor(
            bitmap,
            top = bitmap.height / 2,
            right = bitmap.width,
            bottom = bitmap.height
        )
    }

    fun getHexString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    fun getDominantGradient(
        bitmap: Bitmap,
        cornerRadius: Float? = null,
        orientation: GradientDrawable.Orientation? = null,
        endColor: String? = null
    ): GradientDrawable {
        var dominantColor = getDominantColor(bitmap)

        var endHex = endColor?.let { getHexString(Color.parseColor(it)) }
            ?: getHexString(getLowerSideDominantColor(bitmap))

        if (Color.luminance(dominantColor) > 0.5) {
            dominantColor = getDarkColor(bitmap)
            if (endColor == null) {
                endHex = getHexString(Color.DKGRAY)
            }
        }

        val colors = intArrayOf(dominantColor, Color.parseColor(endHex))
        val actualOrientation = orientation ?: GradientDrawable.Orientation.TOP_BOTTOM

        val gradientDrawable = GradientDrawable(actualOrientation, colors)

        cornerRadius?.let {
            gradientDrawable.cornerRadius = convertDpToPixels(it).toFloat()
        }

        return gradientDrawable
    }

    private fun convertDpToPixels(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun hexToRgb(hex: String): Triple<Int, Int, Int>? {
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
}


