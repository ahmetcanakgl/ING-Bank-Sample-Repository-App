package co.aca.ingrepo.base

import android.app.Activity
import androidx.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentManager
import co.aca.ingrepo.domain.base.BaseUseCase
import co.aca.ingrepo.util.OnBackPressListener
import co.aca.ingrepo.util.UseCaseListener
import co.aca.ingrepo.util.ViewListener
import co.aca.ingrepo.util.alertdialog.DialogContent

abstract class BaseViewModel(vararg cases: BaseUseCase<*, *>) : ViewModel(), OnBackPressListener {

    var arguments: Bundle? = null

    /**
     * Use case'lerde request'leri dispose etmek için use case listesini tutuyoruz
     */
    private var useCases = arrayListOf(*cases)

    private var requestQueue = ArrayList<String>()

    var fragmentManager: FragmentManager? = null
    var viewListener: ViewListener? = null
    var activity: Activity? = null

    @CallSuper
    open fun onCreate(savedInstanceState: Bundle?) {

    }

    @CallSuper
    open fun onCreate(savedInstanceState: Bundle?, arguments: Bundle?) {
    }

    @CallSuper
    open fun onViewCreated(savedInstanceState: Bundle?, arguments: Bundle?) {
        setUseCasesListener()

        onCreate(savedInstanceState)
        onCreate(savedInstanceState, arguments)
    }

    @CallSuper
    open fun onStart() {
    }

    /**
     * Servis response'u geldiginde dialog ve toast gosterilmesi isteniyorsa
     * listener'lar tetiklenir
     */
    private fun setUseCasesListener() {
        for (useCase in useCases) {
            useCase.useCaseListener = object : UseCaseListener {

                override fun showDialog(dgContent: DialogContent) {
                    viewListener?.showDialog(dgContent)
                }

                override fun addToQueue(tag: String) {
                    requestQueue.add(tag)
                    viewListener?.showLoading()
                }

                override fun removeFromQueue(tag: String) {
                    requestQueue.remove(tag)
                    viewListener?.hideLoading()
                }

                override fun timeoutExpired(message: String) {
                }

            }

            useCase.setDialogListener { content -> viewListener?.showDialog(content) }
            useCase.setToastListener { text -> viewListener?.showToast(text, true) }
        }
    }

    @CallSuper
    open fun onResume() {
    }

    @CallSuper
    open fun onSaveInstanceState(outState: Bundle?) {
    }

    /**
     * ViewModel lifecycle'ı sonlandığında çağırılır.
     * RxJava Disposable'larını temizler.
     */
    override fun onCleared() {
        super.onCleared()

        requestQueue.clear()

        viewListener?.hideLoading()

        for (useCase in useCases) {
            useCase.clear()
        }
    }

    fun onDestroyView() {
        for (useCase in useCases) {
            useCase.clear()
        }
    }

    override fun isBackEnable(): Boolean {
        return true
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    }

}