package com.stepanusryan.storyapp.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText

class RyanEditTextEmail : AppCompatEditText {
    constructor(context:Context):super(context){
        init()
    }
    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet){
        init()
    }

    constructor(context: Context,attributeSet: AttributeSet,defAttr:Int):super(context,attributeSet,defAttr){
        init()
    }
    private fun init() {
        addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0?.contains("@")!!){
                    error = "Email not valid"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

}