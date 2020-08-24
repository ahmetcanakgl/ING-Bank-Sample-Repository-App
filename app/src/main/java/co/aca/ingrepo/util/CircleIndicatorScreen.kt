package co.aca.ingrepo.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import co.aca.ingrepo.R

class CircleIndicatorScreen : Dialog {

    constructor(context: Context) : super(context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        }

        setCanceledOnTouchOutside(false)
        setCancelable(false)

        setContentView(R.layout.circle_indicator_screen)
    }
}