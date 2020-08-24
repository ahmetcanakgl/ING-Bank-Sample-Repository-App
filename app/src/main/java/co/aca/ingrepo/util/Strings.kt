package co.aca.ingrepo.util

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class Strings @Inject constructor(private val context: Context) {

    fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }

    fun getString(@StringRes stringId: Int, formatArgs: String): String {
        return context.getString(stringId, formatArgs)
    }
}