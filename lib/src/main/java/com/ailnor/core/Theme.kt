package com.ailnor.core

import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils

object Theme {

    var key_listSelector: String = "key_listSelector"

    var key_actionBarDefault: String = "key_actionBarDefault"

    var key_windowBackgroundWhiteBlackText: String = "key_windowBackgroundWhiteBlackText"

    var key_fastScrollText: String = "key_fastScrollText"

    var key_fastScrollActive: String = "key_fastScrollActive"

    var key_fastScrollInactive: String = "key_fastScrollInactive"

    var key_windowBackgroundWhite: String = "key_windowBackgroundWhite"

    @ColorInt
    const val transparent = 0x0

    @ColorInt
    const val white = -0x1

    @ColorInt
    const val black = -0x1000000

    @ColorInt
    const val red = -0x10000

    @ColorInt
    const val green = -0xff0100

    @ColorInt
    const val yellow = -0x100

    @ColorInt
    const val grey_400 = -0x3C3C3D

    @ColorInt
    const val grey_600 = -0x919192

    @ColorInt
    const val dark_charcoal = -0xcccccd

    @ColorInt
    const val anti_flash_white = -0xf0d0b

    @ColorInt
    const val cultured = -0x121213

    @ColorInt
    const val battleship_grey = -0x79797a

    @ColorInt
    const val platinum = -0x211d19


    internal val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    internal val selectedPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val defaultChatDrawables = HashMap<String, Drawable>()
    private val defaultChatPaints = HashMap<String, Paint>()


    fun init(
        colorPrimary: Int,
        colorOnPrimary: Int,
        colorSecondary: Int,
        colorBackground: Int,
        colorOnBackground: Int,
        colorSurface: Int,
        colorOnSurface: Int,
        appIcon64: Int,
        appIcon128: Int
    ) {

        Theme.colorPrimary = colorPrimary
        Theme.colorOnPrimary = colorOnPrimary
        Theme.colorSecondary = colorSecondary
        Theme.colorBackground = colorBackground
        Theme.colorOnBackground = colorOnBackground
        Theme.colorSurface = colorSurface
        Theme.colorOnSurface = colorOnSurface
        Theme.appIcon64 = appIcon64
        Theme.appIcon128 = appIcon128

        selectedPaint.color = Theme.colorPrimary
        selectedPaint
    }

    fun Int.alpha(@IntRange(from = 0L, to = 100L) factor: Int): Int {
        return ((factor * 255 / 100) shl 24) or (this and 0x00ffffff)
    }

    fun Int.darken(factor: Float) = ColorUtils.blendARGB(
        this, black, factor
    )
    fun Int.lighten(factor: Float) = ColorUtils.blendARGB(
        this, white, factor
    )

    @DrawableRes
    var appIcon64 = 0
        private set
    @DrawableRes
    var appIcon128 = 0
        private set
    @ColorInt
    var colorPrimary = 0x0
        private set
    @ColorInt
    var colorOnPrimary = 0x0
        private set
    @ColorInt
    var colorSecondary = 0x0
        private set
    @ColorInt
    var colorBackground = 0x0
        private set
    @ColorInt
    var colorOnBackground = 0x0
        private set
    @ColorInt
    var colorSurface = 0x0
        private set
    @ColorInt
    var colorOnSurface = 0x0
        private set

    interface ResourcesProvider{
        fun getColor(key: String): Int

        fun getDrawable(drawableKey: String?): Drawable? {
            return null
        }

        fun getPaint(paintKey: String?): Paint? {
            return null
        }
    }

    fun getColor(value: String): Int{
        return transparent
    }

    fun getThemeDrawable(drawableKey: String?): Drawable? {
        return defaultChatDrawables[drawableKey]
    }

    fun getThemePaint(paintKey: String?): Paint? {
        return defaultChatPaints[paintKey]
    }
}
