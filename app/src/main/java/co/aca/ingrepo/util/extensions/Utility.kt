package co.aca.ingrepo.util.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import co.aca.ingrepo.R
import co.aca.ingrepo.util.alertdialog.DialogContent
import co.aca.ingrepo.util.alertdialog.FRAlertDialog
import co.aca.ingrepo.util.fragment.AnimationType
import co.aca.ingrepo.util.fragment.FragmentFactory
import co.aca.ingrepo.util.fragment.TransitionType
import kotlin.reflect.KClass


fun FragmentActivity.showFragment(builder: FragmentFactory?) {
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
        val fragment = builder.getFragment() ?: return

        val tag = builder.getTag()

        val containerId = builder.getViewId()

        if (builder.getAnimationType() !== AnimationType.NO_ANIM) {
            val anim = AnimationType.getAnimation(builder.getAnimationType())

            ft.setCustomAnimations(anim[0], anim[1], anim[2], anim[3])
        }

        if (builder.addToBackStack()) {
            ft.addToBackStack(tag)
        }

        when (builder.getTransitionType()) {
            TransitionType.ADD -> ft.add(containerId, fragment, tag)
            TransitionType.SHOW -> ft.show(fragment)
            TransitionType.HIDE -> ft.hide(fragment)
            else -> ft.replace(containerId, fragment, tag)
        }

        /**
         * commit() changed to commitAllowingStateLoss() for "Can not perform this action after onSaveInstanceState"
         */
        ft.commitAllowingStateLoss()
    }
}

fun FragmentActivity.showDialog(content: DialogContent) {
    val factory = FragmentFactory.Builder(FRAlertDialog.newInstance(content)).build()
    showFragment(factory)
}

fun FragmentActivity.returnPage(clazz: KClass<out Fragment>) {
    val fm = supportFragmentManager
    fm.popBackStack(clazz.java.simpleName, 0)
}

fun Fragment.startActivity(
    flags: Int = -1,
    bundle: Bundle? = null,
    clazz: Class<out AppCompatActivity>? = null
) {
    activity?.startActivity(flags, bundle, clazz)
}

fun FragmentActivity.startActivity(
    flags: Int = -1,
    bundle: Bundle? = null,
    clazz: Class<out AppCompatActivity>? = null
) {
    val intent = Intent(this, clazz)

    if (flags != -1) {
        intent.flags = flags
    }

    if (bundle != null) {
        intent.putExtras(bundle)
    }

    startActivity(intent)
}

fun FragmentActivity.showToast(
    message: String,
    isSuccess: Boolean = true,
    duration: Int = Toast.LENGTH_SHORT
) {
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val layout = inflater.inflate(R.layout.cv_toast, null)
    val scale = this.resources.displayMetrics.density

    val icon = layout.findViewById<AppCompatImageView>(R.id.icon)

    if (!isSuccess)
        icon.setImageDrawable(icon.context.getDrawable(R.drawable.ic_highlight_off_white_24dp))

    val toast = Toast(this)
    toast.setGravity(Gravity.BOTTOM, 0, (80f * scale + 0.5f).toInt())
    toast.duration = duration
    toast.view = layout

    val text = toast.view.findViewById<TextView>(R.id.cvToast_tvText)
    text.text = message
    toast.show()
}
