package co.aca.ingrepo.util.alertdialog

import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import co.aca.ingrepo.domain.BaseParcelableModel

class DialogContent : BaseParcelableModel() {

    @StringRes
    var content: Int = -1
    var contentText: String? = ""

    var btnOrientation: Int = LinearLayout.HORIZONTAL

    var dgType: DialogType? = null

    @StringRes
    var possitiveButton: Int = -1
    @StringRes
    var negativeButton: Int = -1
    @StringRes
    var neutralButton: Int = -1

    var isCancelable = false

    var customFragment: Fragment? = null

    var positiveListener: DialogActionListener? = null
    var negativeListener: DialogActionListener? = null
    var neutralListener: DialogActionListener? = null

    fun setPositiveButton(@StringRes text: Int, positiveActionListener: DialogActionListener?) {
        possitiveButton = text
        positiveListener = positiveActionListener
    }

    fun setNegativeButton(@StringRes text: Int, negativeActionListener: DialogActionListener?) {
        negativeButton = text
        negativeListener = negativeActionListener
    }

    fun setNeutralButton(@StringRes text: Int, neutralActionListener: DialogActionListener?) {
        neutralButton = text
        neutralListener = neutralActionListener
    }

    fun setDialogType(type: DialogType) {
        dgType = type
    }

}