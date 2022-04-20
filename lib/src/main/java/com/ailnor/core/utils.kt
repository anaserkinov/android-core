/* 
 * Copyright Erkinjanov Anaskhan, 14/02/2022.
 */

package com.ailnor.core

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.*
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.StateSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.view.setMargins
import java.util.*
import kotlin.math.ceil

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

fun dp(value: Int) =
    if (value == 0)
        0
    else
        ceil(Utilities.density * value).toInt()

fun dp(value: Float) =
    if (value == 0F)
        0F
    else
        ceil(Utilities.density * value)

val measureSpec_unspecified = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
fun measureSpec_at_most(atMost: Int) =
    View.MeasureSpec.makeMeasureSpec(atMost, View.MeasureSpec.AT_MOST)

fun measureSpec_exactly(exactly: Int) =
    View.MeasureSpec.makeMeasureSpec(exactly, View.MeasureSpec.EXACTLY)

fun groupLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT
) = ViewGroup.LayoutParams(width, height)

fun marginLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    marginLeft: Int = 0,
    marginTop: Int = 0,
    marginRight: Int = 0,
    marginBottom: Int = 0,
): ViewGroup.MarginLayoutParams {
    val layoutParams = ViewGroup.MarginLayoutParams(width, height)
    layoutParams.setMargins(
        marginLeft,
        marginTop,
        marginRight,
        marginBottom
    )
    return layoutParams
}

fun marginLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    margin: Int = 0
): ViewGroup.MarginLayoutParams {
    val layoutParams = ViewGroup.MarginLayoutParams(width, height)
    layoutParams.setMargins(
        margin,
        margin,
        margin,
        margin
    )
    return layoutParams
}

fun frameLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    gravity: Int = Gravity.CENTER
) = FrameLayout.LayoutParams(width, height, gravity)

fun frameLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    gravity: Int = Gravity.CENTER,
    margin: Int = 0
): FrameLayout.LayoutParams {
    val frameLayoutParams = FrameLayout.LayoutParams(width, height, gravity)
    frameLayoutParams.setMargins(margin, margin, margin, margin)
    return frameLayoutParams
}

fun frameLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    gravity: Int = Gravity.CENTER,
    marginLeft: Int = 0,
    marginTop: Int = 0,
    marginRight: Int = 0,
    marginBottom: Int = 0,
): FrameLayout.LayoutParams {
    val frameLayoutParams = FrameLayout.LayoutParams(width, height, gravity)
    frameLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
    return frameLayoutParams
}

fun linearLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    weight: Float = 0F,
): LinearLayout.LayoutParams = LinearLayout.LayoutParams(width, height, weight)

fun linearLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    weight: Float = 0F,
    gravity: Int = Gravity.START,
    margin: Int = 0
): LinearLayout.LayoutParams {
    val layoutParams = LinearLayout.LayoutParams(width, height, weight)
    layoutParams.gravity = gravity
    layoutParams.setMargins(margin)

    return layoutParams
}

fun linearLayoutParams(
    width: Int = WRAP_CONTENT,
    height: Int = WRAP_CONTENT,
    weight: Float = 0F,
    marginLeft: Int = 0,
    marginTop: Int = 0,
    marginRight: Int = 0,
    marginBottom: Int = 0,
    gravity: Int = Gravity.START
): LinearLayout.LayoutParams {
    val layoutParams = LinearLayout.LayoutParams(width, height, weight)
    layoutParams.setMargins(
        marginLeft,
        marginTop,
        marginRight,
        marginBottom
    )
    layoutParams.gravity = gravity

    return layoutParams
}

// Drawables

fun makeRippleDrawable(
    @ColorInt rippleColor: Int = Theme.black.alpha(25),
    @ColorInt backgroundColor: Int = Theme.transparent,
    @ColorInt disabledBackgroundColor: Int = backgroundColor,
    cornerRadius: Float = dp(4f),
    elevation: Float = 0F
) = makeRippleDrawable(
    rippleColor,
    backgroundColor,
    disabledBackgroundColor,
    cornerRadius,
    cornerRadius,
    cornerRadius,
    cornerRadius,
    elevation
)

fun makeRippleDrawable(
    @ColorInt rippleColor: Int = Theme.black.alpha(25),
    @ColorInt backgroundColor: Int = Theme.transparent,
    @ColorInt disabledBackgroundColor: Int = backgroundColor,
    topLeftRadius: Float = dp(4f),
    topRightRadius: Float = dp(4f),
    bottomLeftRadius: Float = dp(4f),
    bottomRightRadius: Float = dp(4f),
    elevataion: Float = 0F
): Drawable {
    val outerRadii = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius
    )
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        val content: GradientDrawable?
        val mask: ShapeDrawable?

        if (backgroundColor == android.graphics.Color.TRANSPARENT) {
            content = null
            mask = ShapeDrawable(RoundRectShape(outerRadii, null, null))
            mask.colorFilter = PorterDuffColorFilter(rippleColor, PorterDuff.Mode.SRC_IN)
        } else {
            content = GradientDrawable()
            content.cornerRadii = outerRadii
            content.color = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_activated),
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    backgroundColor,
                    backgroundColor,
                    disabledBackgroundColor
                )
            )
            mask = null
        }

        val rippleDrawable = RippleDrawable(
            ColorStateList(
                arrayOf(
                    StateSet.WILD_CARD
                ),
                intArrayOf(
                    rippleColor
                )
            ),
            content,
            mask
        )

        rippleDrawable

    } else {

        val shapePressed = ShapeDrawable(RoundRectShape(outerRadii, null, null))
        shapePressed.colorFilter =
            PorterDuffColorFilter(rippleColor, PorterDuff.Mode.SRC_IN)

        val shapeDefault = GradientDrawable().also {
            it.cornerRadii = outerRadii
            it.color = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_activated),
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    backgroundColor,
                    backgroundColor,
                    disabledBackgroundColor
                )
            )
        }

        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(
            intArrayOf(
                android.R.attr.state_pressed,
                android.R.attr.state_enabled
            ), shapePressed
        )
        stateListDrawable.addState(intArrayOf(), shapeDefault)
        stateListDrawable
    }
}


fun makeCircleRippleDrawable(
    @ColorInt rippleColor: Int = Theme.black.alpha(25),
    @ColorInt backgroundColor: Int = Theme.transparent,
    @ColorInt disabledColor: Int = backgroundColor,
    elevataion: Float = 0F
): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        val content: GradientDrawable?
        val mask: GradientDrawable?

        if (backgroundColor == android.graphics.Color.TRANSPARENT) {
            content = null
            mask = GradientDrawable()
            mask.setColor(rippleColor)
            mask.shape = GradientDrawable.OVAL
        } else {
            content = GradientDrawable().also {
                it.shape = GradientDrawable.OVAL
                it.color = ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_activated),
                        intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled)
                    ),
                    intArrayOf(
                        backgroundColor,
                        backgroundColor,
                        disabledColor
                    )
                )
            }
            mask = null
        }

        RippleDrawable(
            ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_pressed),
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(android.R.attr.state_activated)
                ),
                intArrayOf(
                    rippleColor,
                    rippleColor,
                    rippleColor
                )
            ),
            content,
            mask
        )
    } else {

        val shapePressed = GradientDrawable()
        shapePressed.shape = GradientDrawable.OVAL
        shapePressed.setColor(rippleColor)

        val shapeDefault = GradientDrawable().also {
            it.shape = GradientDrawable.OVAL
            it.color = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_activated),
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    backgroundColor,
                    backgroundColor,
                    disabledColor
                )
            )
        }

        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(
            intArrayOf(
                android.R.attr.state_pressed,
                android.R.attr.state_enabled
            ), shapePressed
        )
        stateListDrawable.addState(intArrayOf(), shapeDefault)
        stateListDrawable
    }
}


fun makeRoundedDrawable(
    @ColorInt color: Int = Theme.colorPrimary,
    cornerRadiusTL: Float = 0F,
    cornerRadiusTR: Float = 0F,
    cornerRadiusBL: Float = 0F,
    cornerRadiusBR: Float = 0F
): Drawable {
    val shapeDrawable = ShapeDrawable(
        RoundRectShape(
            floatArrayOf(
                cornerRadiusTL,
                cornerRadiusTL,
                cornerRadiusTR,
                cornerRadiusTR,
                cornerRadiusBR,
                cornerRadiusBR,
                cornerRadiusBL,
                cornerRadiusBL
            ),
            null,
            null
        )
    )
    shapeDrawable.colorFilter =
        PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)

    return shapeDrawable
}

fun makeRoundedDrawable(
    @ColorInt color: Int = Theme.colorPrimary,
    cornerRadius: Float,
    elevataion: Float = 0F
): Drawable {
    val outerRadii = FloatArray(8)
    Arrays.fill(outerRadii, cornerRadius)
    return ShapeDrawable(RoundRectShape(outerRadii, null, null)).also {
        it.colorFilter =
            PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

fun makeCircleDrawable(
    @ColorInt color: Int = Theme.colorPrimary,
    elevataion: Float = 0F
): Drawable {
    return ShapeDrawable(OvalShape()).also {
        it.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}

fun makeOutlinedDrawable(
    @ColorInt strokeColor: Int = Theme.colorPrimary,
    cornerRadius: Float = 0F
): Drawable {
    val outerRadii = FloatArray(8)
    Arrays.fill(outerRadii, cornerRadius)
    val drawable = GradientDrawable()
    drawable.cornerRadii = outerRadii
    drawable.setStroke(dp(1), strokeColor)
    return drawable
}
