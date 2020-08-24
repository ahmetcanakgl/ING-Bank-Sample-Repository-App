package co.aca.ingrepo.util.alertdialog

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import co.aca.ingrepo.R
import co.aca.ingrepo.base.BaseAlertDialog
import co.aca.ingrepo.util.extensions.observe
import kotlinx.android.synthetic.main.fr_alert_dialog.*

class FRAlertDialog : BaseAlertDialog<FRAlertDialogViewModel>() {

    override fun getDialogContent(): DialogContent {
        return viewModel.dialogContent!!
    }

    companion object {
        var DIALOG_CONTENT_BUNDLE_KEY = "dialogContent"

        fun newInstance(dialogContent: DialogContent): FRAlertDialog {
            var data = Bundle()
            data.putParcelable(DIALOG_CONTENT_BUNDLE_KEY, dialogContent)

            var fragment = FRAlertDialog()
            fragment.arguments = data

            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fr_alert_dialog
    }


    override fun setListeners() {

    }

    override fun setReceivers() {
        observe(viewModel.onSetDialogContent) {
            when (it!!.dgType) {
                DialogType.SUCCESS -> imType.setImageResource(R.drawable.ic_add)
                DialogType.UNSUCCESSFUL -> imType.setImageResource(android.R.drawable.ic_lock_idle_low_battery)
//                DialogType.WAITING -> imType.setImageResource(R.drawable.waiting)
//                DialogType.SAVING -> imType.setImageResource(R.drawable.saving)
//                DialogType.INFO -> imType.setImageResource(R.drawable.ic_info)
            }

            if (!TextUtils.isEmpty(it.contentText)) {
                tvMessage.text = it.contentText
            } else {
                tvMessage.text = it.contentText
            }
        }

        observe(viewModel.onSetCustomAlert){
            if(it!!){
                llDefaultAlert.visibility = View.GONE
                flCustomAlert.visibility = View.VISIBLE
            } else{
                flCustomAlert.visibility = View.GONE
                llDefaultAlert.visibility = View.VISIBLE
            }
        }
    }
}