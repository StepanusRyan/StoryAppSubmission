package com.stepanusryan.storyapp.util

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.stepanusryan.storyapp.R

class RyanEditTextEmail : TextInputEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    val isValid: Boolean
        get() = isEmailFormat(text)

    init {
        doOnTextChanged { text, _, _, _ ->
            error = if (!text.isNullOrEmpty() && !isEmailFormat(text)) {
                context.getString(R.string.error_email_invalid)
            } else {
                null
            }
        }
    }

    private fun isEmailFormat(text: CharSequence?): Boolean =
        !text.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(text.trim()).matches()
}
