package com.stepanusryan.storyapp.util

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.stepanusryan.storyapp.R

class RyanEditTextPassword : TextInputEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    val isValid: Boolean
        get() = (text?.length ?: 0) >= MIN_LENGTH

    init {
        doOnTextChanged { text, _, _, _ ->
            val message = if (!text.isNullOrEmpty() && text.length < MIN_LENGTH) {
                context.getString(R.string.error_password_min_length)
            } else {
                null
            }
            setError(message, null)
        }
    }

    private companion object {
        const val MIN_LENGTH = 8
    }
}
