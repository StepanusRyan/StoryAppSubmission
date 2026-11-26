package com.stepanusryan.storyapp.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun View.applySystemBarsPadding(applyTop: Boolean = true) {
    val initialLeft = paddingLeft
    val initialTop = paddingTop
    val initialRight = paddingRight
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        view.updatePadding(
            left = initialLeft + bars.left,
            top = initialTop + if (applyTop) bars.top else 0,
            right = initialRight + bars.right,
            bottom = initialBottom + maxOf(bars.bottom, ime.bottom)
        )
        insets
    }
}

fun Activity.hideKeyboard() {
    WindowCompat.getInsetsController(window, window.decorView)
        .hide(WindowInsetsCompat.Type.ime())
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val Context.currentLocale: Locale
    get() = ConfigurationCompat.getLocales(resources.configuration)[0] ?: Locale.getDefault()

fun String.toDisplayDate(locale: Locale): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val date = runCatching { parser.parse(this) }.getOrNull() ?: return this
    return DateFormat.getDateInstance(DateFormat.LONG, locale).format(date)
}
