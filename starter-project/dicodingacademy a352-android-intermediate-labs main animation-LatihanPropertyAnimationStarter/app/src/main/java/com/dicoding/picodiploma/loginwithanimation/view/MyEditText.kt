package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (hint == "Enter Your Email") {
                    validateEmail(s)
                } else {
                    validatePassword(s)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun validateEmail(email: CharSequence?) {
        val parentLayout = parent.parent as? TextInputLayout
        if (!email.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            parentLayout?.error = "Invalid email format"
        } else {
            parentLayout?.error = null
        }
    }

    private fun validatePassword(password: CharSequence?) {
        val parentLayout = parent.parent as? TextInputLayout
        if (!password.isNullOrEmpty() && password.length < 8) {
            parentLayout?.error = "Password cannot be less than 8 characters"
        } else {
            parentLayout?.error = null
        }
    }
}
