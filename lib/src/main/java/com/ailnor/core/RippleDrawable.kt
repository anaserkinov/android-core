/* 
 * Copyright Erkinjanov Anaskhan, 17/05/22.
 */

package com.ailnor.core

import androidx.annotation.ColorInt

class RippleDrawable{

    class Builder(
        @ColorInt private val rippleColor: Int = Theme.black.alpha(15),
        @ColorInt private val backgroundColor: Int = Theme.transparent,
        private val radius: Int = dp(4)
    )

}