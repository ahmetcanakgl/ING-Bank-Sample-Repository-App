package co.aca.ingrepo.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import co.aca.ingrepo.R
import co.aca.ingrepo.di.ViewModelFactory
import co.aca.ingrepo.util.ViewListener
import co.aca.ingrepo.util.alertdialog.DialogContent
import co.aca.ingrepo.util.extensions.showDialog
import co.aca.ingrepo.util.extensions.showFragment
import co.aca.ingrepo.util.extensions.showToast
import co.aca.ingrepo.util.fragment.FragmentFactory
import dagger.android.support.AndroidSupportInjection
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseAlertDialog<VM : BaseViewModel> : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: VM

    var v: View? = null

    private var container: ViewGroup? = null

    abstract fun getLayoutId(): Int

    abstract fun getDialogContent(): DialogContent

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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container
        if (v != null) {
            (v?.parent as ViewGroup).removeView(v)
        }

        var v = inflater.inflate(R.layout.fr_base_alert_dialog, container, false)

        val container = v.findViewById(R.id.flContainer) as FrameLayout
        val inflatedLayout = layoutInflater.inflate(getLayoutId(), null, false)
        container.addView(inflatedLayout)

        viewModel.fragmentManager = childFragmentManager

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(savedInstanceState, arguments)

        var dgContent = getDialogContent()
        val llButton = view.findViewById(R.id.llButton) as LinearLayout

        val inflatedLayout = if (dgContent.btnOrientation == LinearLayout.VERTICAL) {
            layoutInflater.inflate(R.layout.fr_base_alert_dialog_vertical_btn, null, false)
        } else {
            layoutInflater.inflate(R.layout.fr_base_alert_dialog_horizontal_btn, null, false)
        }
        llButton.addView(inflatedLayout)

        val llPositive = view.findViewById(R.id.llPositive) as LinearLayout
        val llNeutral = view.findViewById(R.id.llNeutral) as LinearLayout
        val llNegative = view.findViewById(R.id.llNegative) as LinearLayout

        val btnNegative = view.findViewById(R.id.btnNegative) as Button
        val btnNeutral = view.findViewById(R.id.btnNeutral) as Button
        val btnPositive = view.findViewById(R.id.btnPositive) as Button

        if (dgContent.negativeButton != -1) {
            llNegative.visibility = View.VISIBLE
            btnNegative.text = getString(dgContent.negativeButton)
            btnNegative.setOnClickListener {
                dismiss()
                dgContent.negativeListener?.onClicked()
            }
        }
        if (dgContent.possitiveButton != -1) {
            llPositive.visibility = View.VISIBLE
            btnPositive.text = getString(dgContent.possitiveButton)
            btnPositive.setOnClickListener {
                dismiss()
                dgContent.positiveListener?.onClicked()
            }
        }
        if (dgContent.neutralButton != -1) {
            llNeutral.visibility = View.VISIBLE
            btnNeutral.text = getString(dgContent.neutralButton)
            btnNeutral.setOnClickListener {
                dismiss()
                dgContent.neutralListener?.onClicked()
            }
        }

        if (dgContent.isCancelable) {
            isCancelable = dgContent.isCancelable
        }

        viewModel.viewListener = object : ViewListener {
            override fun showLoading() {
                (activity as BaseActivity<*>).showLoading()
            }

            override fun hideLoading() {
                (activity as BaseActivity<*>).hideLoading()
            }

            override fun goBack() {
                dismiss()
            }

            override fun returnPage(clazz: KClass<out Fragment>) {

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

}