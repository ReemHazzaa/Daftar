package com.reemhazzaa.daftar.tasks.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideVirtualKeyboard(activity: Activity) {
    val imm: InputMethodManager? =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
}