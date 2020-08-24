package co.aca.ingrepo.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import co.aca.ingrepo.di.ViewModelFactory
import co.aca.ingrepo.util.CircleIndicatorScreen
import co.aca.ingrepo.util.OnBackPressListener
import co.aca.ingrepo.util.ViewListener
import co.aca.ingrepo.util.alertdialog.DialogContent
import co.aca.ingrepo.util.extensions.returnPage
import co.aca.ingrepo.util.extensions.showDialog
import co.aca.ingrepo.util.extensions.showToast
import co.aca.ingrepo.util.fragment.AnimationType
import co.aca.ingrepo.util.fragment.FragmentFactory
import co.aca.ingrepo.util.fragment.TransitionType
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.reflect.KClass


abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var onBackPressListener: OnBackPressListener? = null

    lateinit var viewModel: VM

    private var circleIndicatorScreen: CircleIndicatorScreen? = null

    abstract fun getLayoutId(): Int

    abstract fun setListeners()

    abstract fun setReceivers()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>)

        viewModel.activity = this
        viewModel.fragmentManager = supportFragmentManager
        viewModel.viewListener = object : ViewListener {

            override fun showLoading() {
                this@BaseActivity.showLoading()
            }

            override fun hideLoading() {
                this@BaseActivity.hideLoading()
            }

            override fun goBack() {
                onBackPressed()
            }

            override fun returnPage(clazz: KClass<out Fragment>) {
                this@BaseActivity.returnPage(clazz)
            }

            override fun showDialog(dialogContent: DialogContent) {
                this@BaseActivity.showDialog(dialogContent)
            }

            override fun showToast(text: String, isSuccess: Boolean) {
                this@BaseActivity.showToast(text, isSuccess)
            }

            override fun showFragment(factory: FragmentFactory) {
                this@BaseActivity.showFragment(factory)
            }
        }

        viewModel.onViewCreated(savedInstanceState, intent.extras)

        setListeners()
        setReceivers()
    }

    fun hideLoading() {
        circleIndicatorScreen?.dismiss()
    }

    fun showLoading() {
        if (circleIndicatorScreen == null) {
            circleIndicatorScreen = CircleIndicatorScreen(this)
        }

        if (!circleIndicatorScreen?.isShowing!!) {
            circleIndicatorScreen?.show()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResume()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    open fun showFragment(builder: FragmentFactory?) {
        if (builder == null) {
            throw NullPointerException("Builder can't be null")
        }

        var fm: FragmentManager? = supportFragmentManager

        if (builder.getManager() != null) {
            fm = builder.getManager()
        }

        val ft = fm!!.beginTransaction()

        if (builder.isClearBackStack()) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        if (builder.isDialog()) {
            builder.getDialogFragment()!!.show(fm, null)
        } else {
            val fragment = builder.getFragment()
            val tag = builder.getTag()

            val containerId = builder.getViewId()

            var view = builder.getView()

            if (view == null) {
                view = findViewById(containerId)

                if (view == null) {
                    throw NullPointerException("Please provide a valid view code")
                }
            }

            if (builder.getAnimationType() !== AnimationType.NO_ANIM) {
                val anim = AnimationType.getAnimation(builder.getAnimationType())

                ft.setCustomAnimations(anim[0], anim[1], anim[2], anim[3])
            }

            if (builder.addToBackStack()) {
                ft.addToBackStack(tag)
            }

            when (builder.getTransitionType()) {
                TransitionType.ADD -> ft.add(view.id, fragment!!, tag)
                TransitionType.SHOW -> ft.show(fragment!!)
                TransitionType.HIDE -> ft.hide(fragment!!)
                else -> ft.replace(view.id, fragment!!, tag)
            }
            ft.commitAllowingStateLoss()
        }
    }

    open fun startActivity(activity: BaseActivity<*>) {
        startActivity(Intent(this, activity.javaClass))
    }

    open fun startActivity(activity: BaseActivity<*>, bundle: Bundle) {
        startActivity(Intent(this, activity.javaClass).putExtras(bundle))
    }

    override fun onBackPressed() {
        if (onBackPressListener != null &&
                onBackPressListener!!.isBackEnable()) {
            if (onBackPressListener!!.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

}