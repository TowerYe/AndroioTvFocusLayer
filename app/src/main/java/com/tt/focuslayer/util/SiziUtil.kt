package com.tt.focuslayer.util

import android.content.res.Resources

/**
 * dp转px
 *
 * @param dpValue dp值
 * @return px值
 */
fun dp2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun dp2px(dpValue: Int): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}