/* 
 * Copyright Erkinjanov Anaskhan, 14/02/2022.
 */

package com.ailnor.core

import android.app.Application
import android.os.Handler

open class Application: Application() {

    companion object{
        lateinit var handler: Handler
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(applicationContext.mainLooper)
        Core.applicationContext = this
    }

}