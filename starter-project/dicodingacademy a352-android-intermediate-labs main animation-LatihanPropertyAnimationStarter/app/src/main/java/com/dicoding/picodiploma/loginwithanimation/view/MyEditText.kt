package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.picodiploma.loginwithanimation.R



class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs){

    init {
        // Menambahkan TextWatcher untuk validasi password
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak digunakan
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = parent.parent
                if (parent is com.google.android.material.textfield.TextInputLayout) {
                    if (!s.isNullOrEmpty() && s.length < 8) {
                        parent.error = context.getString(R.string.password_too_short)
                    } else {
                        parent.error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak digunakan
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Masukkan Password Anda"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}

