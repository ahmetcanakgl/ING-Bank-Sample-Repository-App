package co.aca.ingrepo.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import co.aca.ingrepo.di.ViewModelFactory
import co.aca.ingrepo.util.ViewListener
import co.aca.ingrepo.util.alertdialog.DialogContent
import co.aca.ingrepo.util.extensions.returnPage
import co.aca.ingrepo.util.extensions.showDialog
import co.aca.ingrepo.util.extensions.showFragment
import co.aca.ingrepo.util.extensions.showToast
import co.aca.ingrepo.util.fragment.FragmentFactory
import dagger.android.support.AndroidSupportInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: VM

    private var container: ViewGroup? = null
    private var mView: View? = null

    abstract fun getLayoutId(): Int

    abstract fun setListeners()

    abstract fun setReceivers()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)

        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>)
        }

        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container

        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false)
        } else {
            if (mView?.parent != null) {
                (mView?.parent as ViewGroup)?.removeView(mView)
            }
        }

        viewModel.fragmentManager = childFragmentManager

        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.activity = activity

        /**
         * ViewModel'e listener setlenir
         */
        viewModel.viewListener = object : ViewListener {

            override fun showLoading() {
                (activity as BaseActivity<*>)?.showLoading()
            }

            override fun hideLoading() {
                (activity as BaseActivity<*>)?.hideLoading()
            }

            override fun goBack() {
                activity?.onBackPressed()
            }

            override fun returnPage(clazz: KClass<out Fragment>) {
                activity?.returnPage(clazz)
            }

            override fun showDialog(dialogContent: DialogContent) {
                activity?.showDialog(dialogContent)
            }

            override fun showToast(text: String, isSuccess: Boolean) {
                activity?.showToast(text)
            }

            override fun showFragment(factory: FragmentFactory) {
                if (factory.mViewId == -1) {
                    factory.mViewId = container?.id!!
                }

                activity?.showFragment(factory)
            }
        }

        viewModel.onViewCreated(savedInstanceState, arguments)

        setListeners()
        setReceivers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.arguments = arguments
        viewModel.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()

    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        viewModel.onDestroyView()
        super.onDestroyView()
    }

    fun showFragment(fragment: BaseFragment<*>, id: Int) {
        var factory = FragmentFactory.Builder(fragment)
                .setViewId(id)
                .build()

        (activity as BaseActivity<*>).showFragment(factory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}