package com.reemhazzaa.daftar.tasks

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.data.tasks.models.Priority

fun hideVirtualKeyboard(activity: Activity) {
    val imm: InputMethodManager? =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
}

fun parsePriorityIntToString(priority: Int): Priority {
    return when (priority) {
        0 -> Priority.HIGH
        1 -> Priority.MEDIUM
        2 -> Priority.LOW
        else -> Priority.LOW
    }
}

fun parsePriorityToInt(priority: Priority): Int {
    return when(priority) {
        Priority.HIGH -> 0
        Priority.MEDIUM -> 1
        Priority.LOW -> 2
    }
}

fun parseItemBackgroundBasedOnPriority(ctx: Context, priority: Priority): Int {
    return when (priority) {
        Priority.HIGH -> ContextCompat.getColor(ctx, R.color.red_light)
        Priority.MEDIUM -> ContextCompat.getColor(ctx, R.color.yellow_light)
        Priority.LOW -> ContextCompat.getColor(ctx, R.color.green_light)
    }
}

fun parseIndicatorBackgroundBasedOnPriority(ctx: Context, priority: Priority): Int {
    return when (priority) {
        Priority.HIGH -> ContextCompat.getColor(ctx, R.color.red_dark)
        Priority.MEDIUM -> ContextCompat.getColor(ctx, R.color.yellow_dark)
        Priority.LOW -> ContextCompat.getColor(ctx, R.color.green_dark)
    }
}

fun spinnerListener(ctx: Context): AdapterView.OnItemSelectedListener = object :
    AdapterView.OnItemSelectedListener{
    override fun onNothingSelected(p0: AdapterView<*>?) {}
    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        when(position){
            0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(ctx, R.color.red_dark)) }
            1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(ctx, R.color.yellow_dark)) }
            2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(ctx, R.color.green_dark)) }
        }
    }
}
