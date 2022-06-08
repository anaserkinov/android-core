package com.ailnor.core

import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange

object Theme {

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
    }

    fun Int.alpha(@IntRange(from = 0L, to = 100L) factor: Int): Int {
        return ((factor * 255 / 100) shl 24) or (this and 0x00ffffff)
    }

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


    internal val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)

}
