
package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: CharSequence?) {
        val parentLayout = parent.parent as? TextInputLayout
        if (!password.isNullOrEmpty() && password.length < 8) {
            parentLayout?.error = "Password tidak boleh kurang dari 8 karakter"
        } else {
            parentLayout?.error = null
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan Password Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}


