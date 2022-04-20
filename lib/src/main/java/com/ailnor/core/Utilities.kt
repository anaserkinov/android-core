/* 
 * Copyright Erkinjanov Anaskhan, 14/02/2022.
 */

package com.ailnor.core

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.SparseArray
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.util.forEach
import java.lang.reflect.Field
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.ceil

object Utilities {

    private var adjustOwnerId = 0
    private var lastFragmentId = 1
    var density = 1f
    var displaySize = Point()
    var displayMetrics = DisplayMetrics()
    var statusBarHeight = 0
    var isTablet = false
        private set
    var isPortrait = true
    var isInMultiWindow = false
    private var mAttachInfoField: Field? = null
    private var mStableInsetsField: Field? = null

    private var broadcasting = 0
    private val addAfterBroadcast = SparseArray<ArrayList<ActionListener>>()
    private val removeAfterBroadcast = SparseArray<ArrayList<ActionListener>>()
    private val listeners = SparseArray<ArrayList<ActionListener>>()

    val isLandscapeTablet: Boolean
        get() = isTablet && !isPortrait

    fun checkDisplaySize(context: Context, newConfiguration: Configuration?) {
        isPortrait = if (newConfiguration != null)
            newConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
        else
            context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        isTablet = context.resources.getBoolean(R.bool.isTablet)
        try {
//            val oldDensity: Float = density
            density = context.resources.displayMetrics.density
//            val newDensity: Float = density
//            if (firstConfigurationWas && abs(oldDensity - newDensity) > 0.001) {
//                Theme.reloadAllResources(context)
//            }
//            firstConfigurationWas = true
            var configuration = newConfiguration
            if (configuration == null) {
                configuration = context.resources.configuration
            }
//            usingHardwareInput = configuration!!.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = manager.defaultDisplay
            if (display != null) {
                display.getMetrics(displayMetrics)
                display.getSize(displaySize)
            }
            if (configuration != null && configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                val newSize =
                    ceil((configuration.screenWidthDp * density).toDouble())
                        .toInt()
                if (abs(displaySize.x - newSize) > 3)
                    displaySize.x = newSize
            }
            if (configuration != null && configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                val newSize =
                    ceil((configuration.screenHeightDp * density).toDouble())
                        .toInt()
                if (abs(displaySize.y - newSize) > 3)
                    displaySize.y = newSize
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPixelsInCM(cm: Float, isX: Boolean): Float {
        return cm / 2.54f * if (isX) displayMetrics.xdpi else displayMetrics.ydpi
    }

    fun fillStatusBarHeight(context: Context?) {
        if (context == null || statusBarHeight > 0) {
            return
        }
        statusBarHeight = getStatusBarHeight(context)
    }

    fun setLightStatusBar(window: Window, enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            if (enable) {
                if (flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR == 0) {
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    decorView.systemUiVisibility = flags
//                    if (!SharedConfig.noStatusBar) {
//                        window.statusBarColor = 0x0f000000
//                    }
                }
            } else {
                if (flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0) {
                    flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    decorView.systemUiVisibility = flags
//                    if (!SharedConfig.noStatusBar) {
//                        window.statusBarColor = 0x33000000
//                    }
                }
            }
        }
    }

    fun setLightNavigationBar(window: Window, enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = if (enable) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            decorView.systemUiVisibility = flags
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    fun getViewInset(view: View?): Int {
        if (view == null || view.height == displaySize.y || view.height == displaySize.y - statusBarHeight) {
            return 0
        }
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                val insets = view.rootWindowInsets
                return insets?.stableInsetBottom ?: 0
            } else {
                if (mAttachInfoField == null) {
                    mAttachInfoField = View::class.java.getDeclaredField("mAttachInfo")
                    mAttachInfoField!!.isAccessible = true
                }
                val mAttachInfo: Any = mAttachInfoField!!.get(view)
                if (mAttachInfo != null) {
                    if (mStableInsetsField == null) {
                        mStableInsetsField = mAttachInfo.javaClass.getDeclaredField("mStableInsets")
                        mStableInsetsField!!.isAccessible = true
                    }
                    val insets = mStableInsetsField!!.get(mAttachInfo) as Rect
                    return insets.bottom
                }
            }
        } catch (e: Exception) {
        }
        return 0
    }

    fun addActionListener(key: Int, actionListener: ActionListener) {
        if (broadcasting != 0) {
            var observers = addAfterBroadcast[key]
            if (observers == null) {
                observers = ArrayList()
                addAfterBroadcast.put(key, observers)
            }
            observers.add(actionListener)
            return
        }
        var observables = listeners.get(key)
        if (observables == null) {
            observables = arrayListOf()
            listeners.put(key, observables)
        } else if (observables.contains(actionListener))
            return
        observables.add(actionListener)
    }

    fun removeActionListener(key: Int, actionListener: ActionListener) {
        if (broadcasting != 0) {
            var observers = removeAfterBroadcast[key]
            if (observers == null) {
                observers = ArrayList()
                removeAfterBroadcast.put(key, observers)
            }
            observers.add(actionListener)
            return
        }
        listeners.get(key)?.remove(actionListener)
    }

    fun onAction(key: Int) {
        onAction(key, 0)
    }

    fun onAction(key: Int, action: Int, vararg data: Any?) {
        Application.handler.post {
            broadcasting++
            listeners[key]?.forEach {
                it.onAction(action, *data)
            }
            broadcasting--
            if (broadcasting == 0) {
                if (removeAfterBroadcast.size() != 0) {
                    removeAfterBroadcast.forEach { key, value ->
                        value.forEach {
                            removeActionListener(key, it)
                        }
                    }
                    removeAfterBroadcast.clear()
                }
                if (addAfterBroadcast.size() != 0) {
                    addAfterBroadcast.forEach { key, value ->
                        value.forEach {
                            addActionListener(key, it)
                        }
                    }
                    addAfterBroadcast.clear()
                }
            }
        }
    }

    fun runOnUIThread(runnable: Runnable) {
        runOnUIThread(runnable, 0)
    }

    fun runOnUIThread(runnable: Runnable, delay: Long) {
        if (delay == 0L) {
            Application.handler.post(runnable)
        } else {
            Application.handler.postDelayed(runnable, delay)
        }
    }

    fun cancelRunOnUIThread(runnable: Runnable) {
        Application.handler.removeCallbacks(runnable)
    }

    fun generateFragmentId() = lastFragmentId ++

    fun requestAdjustResize(activity: Activity?, fragmentId: Int) {
        if (activity == null || isTablet) {
            return
        }
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        adjustOwnerId= fragmentId
    }

    fun requestAdjustNothing(activity: Activity?, fragmentId: Int) {
        if (activity == null || isTablet) {
            return
        }
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        adjustOwnerId = fragmentId
    }

    fun setAdjustResizeToNothing(activity: Activity?, fragmentId: Int) {
        if (activity == null || isTablet) {
            return
        }
        if (adjustOwnerId == 0 || adjustOwnerId == fragmentId) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
    }

    fun removeAdjustResize(activity: Activity?, fragmentId: Int) {
        if (activity == null || isTablet) {
            return
        }
        if (adjustOwnerId == fragmentId) {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }


}