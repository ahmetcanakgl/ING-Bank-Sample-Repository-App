package co.aca.ingrepo.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class Util {

    companion object {
        fun hideKeyboard(context: Context) {
            try {
                val v = (context as Activity).currentFocus
                v!!.clearFocus()
                v.isFocusable = false
                v.isFocusableInTouchMode = true

                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            } catch (e: Exception) {
            }
        }
    }
}