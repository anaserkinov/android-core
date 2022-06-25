/* 
 * Copyright Erkinjanov Anaskhan, 14/02/2022.
 */

package com.ailnor.core

import android.R
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.RippleDrawable
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
import com.ailnor.core.Theme.maskPaint
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

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

fun dp2(value: Int) =
    if (value == 0)
        0
    else
        floor(Utilities.density * value).toInt()


fun dp2(value: Float) =
    if (value == 0F)
        0F
    else
        Utilities.density * value

fun dpr(value: Float): Int {
    return if (value == 0f) {
        0
    } else (Utilities.density * value).roundToInt()
}

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
    @ColorInt rippleColor: Int = Theme.platinum.alpha(70),
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
    @ColorInt rippleColor: Int = Theme.platinum.alpha(70),
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

        if (backgroundColor == Theme.transparent) {
            content = null
            mask = ShapeDrawable(RoundRectShape(outerRadii, null, null))
            mask.colorFilter = PorterDuffColorFilter(rippleColor, PorterDuff.Mode.SRC_IN)
        } else {
            content = GradientDrawable()
            content.cornerRadii = outerRadii
            content.color = ColorStateList(
                arrayOf(
                    intArrayOf(R.attr.state_activated),
                    intArrayOf(R.attr.state_enabled),
                    intArrayOf(-R.attr.state_enabled)
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
                    intArrayOf(R.attr.state_activated),
                    intArrayOf(R.attr.state_enabled),
                    intArrayOf(-R.attr.state_enabled)
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
                R.attr.state_pressed,
                R.attr.state_enabled
            ), shapePressed
        )
        stateListDrawable.addState(intArrayOf(), shapeDefault)
        stateListDrawable
    }
}


fun makeCircleRippleDrawable(
    @ColorInt rippleColor: Int = Theme.platinum.alpha(70),
    @ColorInt backgroundColor: Int = Theme.transparent,
    @ColorInt disabledColor: Int = backgroundColor,
    elevataion: Float = 0F
): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        val content: GradientDrawable?
        val mask: GradientDrawable?

        if (backgroundColor == Theme.transparent) {
            content = null
            mask = GradientDrawable()
            mask.setColor(rippleColor)
            mask.shape = GradientDrawable.OVAL
        } else {
            content = GradientDrawable().also {
                it.shape = GradientDrawable.OVAL
                it.color = ColorStateList(
                    arrayOf(
                        intArrayOf(R.attr.state_activated),
                        intArrayOf(R.attr.state_enabled),
                        intArrayOf(-R.attr.state_enabled)
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
                    intArrayOf(R.attr.state_pressed),
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(R.attr.state_activated)
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
                    intArrayOf(R.attr.state_activated),
                    intArrayOf(R.attr.state_enabled),
                    intArrayOf(-R.attr.state_enabled)
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
                R.attr.state_pressed,
                R.attr.state_enabled
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


// From telegram

fun setCombinedDrawableColor(combinedDrawable: Drawable?, color: Int, isIcon: Boolean) {
    if (combinedDrawable !is CombinedDrawable) {
        return
    }
    val drawable: Drawable = if (isIcon) {
        combinedDrawable.icon
    } else {
        combinedDrawable.background
    }
    if (drawable is ColorDrawable) {
        drawable.color = color
    } else {
        drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }
}

fun createSimpleSelectorCircleDrawable(size: Int, defaultColor: Int, pressedColor: Int): Drawable? {
    val ovalShape = OvalShape()
    ovalShape.resize(size.toFloat(), size.toFloat())
    val defaultDrawable = ShapeDrawable(ovalShape)
    defaultDrawable.paint.color = defaultColor
    val pressedDrawable = ShapeDrawable(ovalShape)
    pressedDrawable.paint.color = -0x1
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(pressedColor))
    return RippleDrawable(colorStateList, defaultDrawable, pressedDrawable)
}

fun createRoundRectDrawable(rad: Int, defaultColor: Int): Drawable {
    val defaultDrawable = ShapeDrawable(
        RoundRectShape(
            floatArrayOf(
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat()
            ), null, null
        )
    )
    defaultDrawable.paint.color = defaultColor
    return defaultDrawable
}


fun createSimpleSelectorRoundRectDrawable(
    rad: Int,
    defaultColor: Int,
    pressedColor: Int,
    maskColor: Int = pressedColor
): Drawable {
    val defaultDrawable = ShapeDrawable(
        RoundRectShape(
            floatArrayOf(
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat()
            ), null, null
        )
    )
    defaultDrawable.paint.color = defaultColor
    val pressedDrawable = ShapeDrawable(
        RoundRectShape(
            floatArrayOf(
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat(),
                rad.toFloat()
            ), null, null
        )
    )
    pressedDrawable.paint.color = maskColor
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(pressedColor))
    return RippleDrawable(colorStateList, defaultDrawable, pressedDrawable)
}

fun createSelectorDrawableFromDrawables(normal: Drawable?, pressed: Drawable?): Drawable {
    val stateListDrawable = StateListDrawable()
//    stateListDrawable.addState(intArrayOf(R.attr.state_pressed), pressed)
    stateListDrawable.addState(intArrayOf(R.attr.state_selected), pressed)
    stateListDrawable.addState(StateSet.WILD_CARD, normal)
    return stateListDrawable
}

fun getRoundRectSelectorDrawable(corners: Int = dp(3), color: Int): Drawable {
    val maskDrawable = createRoundRectDrawable(corners, -0x1)
    val colorStateList = ColorStateList(
        arrayOf(StateSet.WILD_CARD), intArrayOf(
            color and 0x00ffffff or 0x19000000
        )
    )
    return RippleDrawable(colorStateList, null, maskDrawable)
}

fun createSelectorWithBackgroundDrawable(backgroundColor: Int, color: Int): Drawable {
    val maskDrawable: Drawable = ColorDrawable(backgroundColor)
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(color))
    return RippleDrawable(colorStateList, ColorDrawable(backgroundColor), maskDrawable)
}

fun getSelectorDrawable(color: Int = Theme.red, whiteBackground: Boolean): Drawable {
    return if (whiteBackground) {
        getSelectorDrawable(color, Theme.green)
    } else {
        createSelectorDrawable(color, 2)
    }
}

fun getSelectorDrawable(color: Int, backgroundColor: Int?): Drawable {
    return if (backgroundColor != null) {
        val maskDrawable: Drawable = ColorDrawable(-0x1)
        val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(color))
        RippleDrawable(
            colorStateList,
            ColorDrawable(backgroundColor),
            maskDrawable
        )
    } else
        createSelectorDrawable(color, 2)
}


fun createSelectorDrawable(color: Int, maskType: Int = 1, radius: Int = -1): Drawable {
    var maskDrawable: Drawable? = null
    if ((maskType == 1 || maskType == 5) && Build.VERSION.SDK_INT >= 23) {
        maskDrawable = null
    } else if (maskType == 1 || maskType == 3 || maskType == 4 || maskType == 5 || maskType == 6 || maskType == 7) {
        maskPaint.color = -0x1
        maskDrawable = object : Drawable() {
            var rect: RectF? = null
            override fun draw(canvas: Canvas) {
                val bounds = bounds
                if (maskType == 7) {
                    if (rect == null) {
                        rect = RectF()
                    }
                    rect!!.set(bounds)
                    canvas.drawRoundRect(
                        rect!!,
                        dp(6f),
                        dp(6f),
                        maskPaint
                    )
                } else {
                    val rad = when (maskType) {
                        1, 6 -> {
                            dp(20)
                        }
                        3 -> {
                            bounds.width().coerceAtLeast(bounds.height()) / 2
                        }
                        else -> {
                            ceil(sqrt(((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX()) + (bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY())).toDouble()))
                                .toInt()
                        }
                    }
                    canvas.drawCircle(
                        bounds.centerX().toFloat(),
                        bounds.centerY().toFloat(),
                        rad.toFloat(),
                        maskPaint
                    )
                }
            }

            override fun setAlpha(alpha: Int) {}
            override fun setColorFilter(colorFilter: ColorFilter?) {}
            override fun getOpacity(): Int {
                return PixelFormat.UNKNOWN
            }
        }
    } else if (maskType == 2) {
        maskDrawable = ColorDrawable(-0x1)
    }
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(color))
    val rippleDrawable = RippleDrawable(colorStateList, null, maskDrawable)
    if (Build.VERSION.SDK_INT >= 23) {
        if (maskType == 1) {
            rippleDrawable.radius = if (radius <= 0) dp(20) else radius
        } else if (maskType == 5) {
            rippleDrawable.radius = RippleDrawable.RADIUS_AUTO
        }
    }
    return rippleDrawable
}

fun createCircleSelectorDrawable(color: Int, leftInset: Int, rightInset: Int): Drawable {
    maskPaint.setColor(-0x1)
    val maskDrawable: Drawable = object : Drawable() {
        override fun draw(canvas: Canvas) {
            val bounds = bounds
            val rad = Math.max(bounds.width(), bounds.height()) / 2 + leftInset + rightInset
            canvas.drawCircle(
                (bounds.centerX() - leftInset + rightInset).toFloat(),
                bounds.centerY().toFloat(),
                rad.toFloat(),
                maskPaint
            )
        }

        override fun setAlpha(alpha: Int) {}
        override fun setColorFilter(colorFilter: ColorFilter?) {}
        override fun getOpacity(): Int {
            return PixelFormat.UNKNOWN
        }
    }
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(color))
    return RippleDrawable(colorStateList, null, maskDrawable)
}

class RippleRadMaskDrawable : Drawable {
    private val path = Path()
    private val rect = RectF()
    private val radii = FloatArray(8)

    constructor(top: Float, bottom: Float) {
        radii[3] = dp(top)
        radii[2] = radii[3]
        radii[1] = radii[2]
        radii[0] = radii[1]
        radii[7] = dp(bottom)
        radii[6] = radii[7]
        radii[5] = radii[6]
        radii[4] = radii[5]
    }

    constructor(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        radii[1] = dp(topLeft)
        radii[0] = radii[1]
        radii[3] = dp(topRight)
        radii[2] = radii[3]
        radii[5] = dp(bottomRight)
        radii[4] = radii[5]
        radii[7] = dp(bottomLeft)
        radii[6] = radii[7]
    }

    fun setRadius(top: Float, bottom: Float) {
        radii[3] = dp(top)
        radii[2] = radii[3]
        radii[1] = radii[2]
        radii[0] = radii[1]
        radii[7] = dp(bottom)
        radii[6] = radii[7]
        radii[5] = radii[6]
        radii[4] = radii[5]
        invalidateSelf()
    }

    fun setRadius(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        radii[1] = dp(topLeft)
        radii[0] = radii[1]
        radii[3] = dp(topRight)
        radii[2] = radii[3]
        radii[5] = dp(bottomRight)
        radii[4] = radii[5]
        radii[7] = dp(bottomLeft)
        radii[6] = radii[7]
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        rect.set(bounds)
        path.addRoundRect(rect, radii, Path.Direction.CW)
        canvas.drawPath(path, maskPaint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}

fun setMaskDrawableRad(rippleDrawable: Drawable?, top: Int, bottom: Int) {
    if (rippleDrawable is RippleDrawable) {
        val drawable = rippleDrawable
        val count = drawable.numberOfLayers
        for (a in 0 until count) {
            val layer = drawable.getDrawable(a)
            if (layer is RippleRadMaskDrawable) {
                drawable.setDrawableByLayerId(
                    R.id.mask, RippleRadMaskDrawable(
                        top.toFloat(),
                        bottom.toFloat()
                    )
                )
                break
            }
        }
    }
}

fun createRadSelectorDrawable(color: Int, topRad: Int, bottomRad: Int): Drawable {
    maskPaint.setColor(-0x1)
    val maskDrawable: Drawable = RippleRadMaskDrawable(
        topRad.toFloat(),
        bottomRad.toFloat()
    )
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(color))
    return RippleDrawable(colorStateList, null, maskDrawable)
}

fun createRadSelectorDrawable(
    color: Int,
    topLeftRad: Int,
    topRightRad: Int,
    bottomRightRad: Int,
    bottomLeftRad: Int
): Drawable {
    maskPaint.setColor(-0x1)
    val maskDrawable: Drawable = RippleRadMaskDrawable(
        topLeftRad.toFloat(),
        topRightRad.toFloat(), bottomRightRad.toFloat(), bottomLeftRad.toFloat()
    )
    val colorStateList = ColorStateList(arrayOf(StateSet.WILD_CARD), intArrayOf(color))
    return RippleDrawable(colorStateList, null, maskDrawable)
}

