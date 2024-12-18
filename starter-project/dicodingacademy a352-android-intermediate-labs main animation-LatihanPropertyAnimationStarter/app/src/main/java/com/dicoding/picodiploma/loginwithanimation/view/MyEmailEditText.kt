package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyEmailEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validateEmail(email: CharSequence?) {
        val parentLayout = parent.parent as? TextInputLayout
        when {
            email.isNullOrEmpty() -> parentLayout?.error = null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> parentLayout?.error = "Invalid email format"
            else -> parentLayout?.error = null
        }
    }
}
