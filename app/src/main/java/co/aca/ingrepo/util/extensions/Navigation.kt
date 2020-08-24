package co.aca.ingrepo.util.extensions

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import co.aca.ingrepo.R
import co.aca.ingrepo.base.BaseActivity
import co.aca.ingrepo.base.BaseFragment
import co.aca.ingrepo.base.BaseViewModel
import co.aca.ingrepo.util.alertdialog.DialogActionListener
import co.aca.ingrepo.util.alertdialog.DialogContent
import co.aca.ingrepo.util.alertdialog.DialogType
import co.aca.ingrepo.util.alertdialog.FRAlertDialog
import co.aca.ingrepo.util.fragment.AnimationType
import co.aca.ingrepo.util.fragment.FragmentFactory
import co.aca.ingrepo.util.fragment.TransitionType

/**
 * fragmentManager BaseActivity'den ve BaseFragment'dan her bir view'in ViewModel'ine set edilmektedir.
 *
 * @see BaseFragment.onCreateView
 * @see BaseActivity.onCreate
 *
 * Activity supportFragmentManager kullanirken, Fragment'lar childFragmentManager kullanmaktadir.
 * inner fragment kullanim durumunda eğer backstack kullanilmayacak ise fragmentManagerEnable true gonderilmesi gerekmektedir.
 * backPress override manuel pop edilmelidir
 *
 * @see BaseViewModel.fragmentManager
 */
fun BaseViewModel.navigateToFragment(fragment: Fragment,
                                     addToBackStack: Boolean = true,
                                     fragmentManagerEnable: Boolean = false,
                                     clearBackStack: Boolean = false,
                                     viewId: Int = R.id.container,
                                     transitionType: TransitionType = TransitionType.REPLACE,
                                     animation: AnimationType = AnimationType.ENTER_FROM_RIGHT) {

    val factory = FragmentFactory.Builder(fragment)
            .addToBackStack(addToBackStack)
            .setFragmentManager(if (fragmentManagerEnable) fragmentManager else null)
            .setClearBackStack(clearBackStack)
            .setViewId(viewId)
            .setTransitionType(transitionType)
            .setAnimation(animation)
            .build()

    viewListener?.showFragment(factory)

}


inline fun <reified T : Any> BaseViewModel.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(activity!!.applicationContext)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        activity!!.startActivityForResult(intent, requestCode, options)
    } else {
        activity!!.startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)


/**
 * AlertDialog göstermek için kullanılır.Standart Success,Info,Warning pop upları dışında bir alert
 * gösterilmek istenilirse custom fragment set edilerek kullanılır.
 */
fun BaseViewModel.showDialog(contentText: String = "",
                             dgType: DialogType = DialogType.SUCCESS,
                             customFragment: Fragment? = null,
                             isCancelable: Boolean = true,
                             btnOrientation: Int = android.widget.LinearLayout.HORIZONTAL,
                             positiveButton: Int = -1,
                             positiveButtonListener: DialogActionListener? = null,
                             neutralButtonListener: DialogActionListener? = null,
                             neutralButton: Int = -1,
                             negativeButton: Int = -1,
                             negativeButtonListener: DialogActionListener? = null) {

    val dgContent = DialogContent()
    dgContent.contentText = contentText
    dgContent.dgType = dgType
    dgContent.isCancelable = isCancelable
    dgContent.btnOrientation = btnOrientation
    dgContent.possitiveButton = positiveButton
    dgContent.neutralButton = neutralButton
    dgContent.negativeButton = negativeButton
    dgContent.customFragment = customFragment
    dgContent.setPositiveButton(positiveButton, positiveButtonListener)
    dgContent.setNegativeButton(negativeButton, negativeButtonListener)
    dgContent.setNeutralButton(neutralButton, neutralButtonListener)

    navigateToFragment(FRAlertDialog.newInstance(dgContent))
}

