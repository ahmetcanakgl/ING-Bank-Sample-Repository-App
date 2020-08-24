package co.aca.ingrepo.util.alertdialog

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import co.aca.ingrepo.R
import co.aca.ingrepo.base.BaseViewModel
import co.aca.ingrepo.util.extensions.navigateToFragment
import javax.inject.Inject

class FRAlertDialogViewModel @Inject constructor() : BaseViewModel() {

    var dialogContent: DialogContent? = null
    lateinit var onSetDialogContent: MutableLiveData<DialogContent>
    lateinit var onSetCustomAlert: MutableLiveData<Boolean>

    override fun onViewCreated(savedInstanceState: Bundle?, arguments: Bundle?) {
        super.onViewCreated(savedInstanceState, arguments)

        if (arguments != null) {
            dialogContent = arguments.getParcelable(FRAlertDialog.DIALOG_CONTENT_BUNDLE_KEY)
        }

        onSetDialogContent = MutableLiveData()
        onSetCustomAlert = MutableLiveData()

        onSetDialogContent.postValue(dialogContent)

        if (dialogContent?.customFragment != null) {
            onSetCustomAlert.postValue(true)
            navigateToFragment(
                fragment = dialogContent?.customFragment!!,
                fragmentManagerEnable = true,
                viewId = R.id.flCustomAlert
            )
        } else {
            onSetCustomAlert.postValue(false)

        }

    }
}