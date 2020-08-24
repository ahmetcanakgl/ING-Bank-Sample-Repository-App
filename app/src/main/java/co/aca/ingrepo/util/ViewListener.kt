package co.aca.ingrepo.util

import androidx.fragment.app.Fragment
import co.aca.ingrepo.util.alertdialog.DialogContent
import co.aca.ingrepo.util.fragment.FragmentFactory
import kotlin.reflect.KClass

interface ViewListener {

    fun showDialog(dialogContent: DialogContent)

    fun showToast(text: String, isSuccess: Boolean)

    fun showFragment(factory: FragmentFactory)

    fun showLoading()

    fun hideLoading()

    fun goBack()

    fun returnPage(clazz: KClass<out Fragment>)


}