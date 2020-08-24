package co.aca.ingrepo.util

import android.content.SharedPreferences
import android.preference.PreferenceManager
import co.aca.ingrepo.base.App
import co.aca.ingrepo.repository.response.UserRepo

class Preferences {

    object Keys {
        const val USER_FAV_LIST = "USER_FAV_LIST"
    }

    companion object {
        // --- Objects --- //
        private var pref: SharedPreferences? = null

        private fun getPreferences(): SharedPreferences {
            if (pref == null) {
                val context = App.getInstance()!!.applicationContext
                pref = PreferenceManager.getDefaultSharedPreferences(context)
                pref!!.edit().commit()
            }
            return pref as SharedPreferences
        }

        fun getString(key: String, defValue: String): String? {
            return getPreferences().getString(key, defValue)
        }

        fun getString(key: String): String {
            return getPreferences().getString(key, "").toString()
        }

        fun deleteKey(key: String) {
            val editor = getPreferences().edit()
            editor.remove(key)
            editor.apply()
        }

        fun setString(key: String, newValue: String) {
            val editor = getPreferences().edit()
            editor.putString(key, newValue)
            editor.commit()
        }

        fun setInt(key: String, newValue: Int) {
            val editor = getPreferences().edit()
            editor.putInt(key, newValue)
            editor.commit()
        }

        fun setBoolean(key: String, newValue: Boolean?) {
            val editor = getPreferences().edit()
            editor.putBoolean(key, newValue!!)
            editor.commit()
        }

        fun setStringSet(key: String, newValue: Set<String>?) {
            val editor = getPreferences().edit()
            editor.putStringSet(key, newValue!!)
            editor.commit()
        }

        fun getInt(key: String, defValue: Int): Int {
            return getPreferences().getInt(key, defValue)
        }

        fun getFloat(key: String, defValue: Float): Float {
            return getPreferences().getFloat(key, defValue)
        }

        fun getLong(key: String, defValue: Long): Long {
            return getPreferences().getLong(key, defValue)
        }

        fun getBoolean(key: String, defValue: Boolean): Boolean {
            return getPreferences().getBoolean(key, defValue)
        }

        fun getStringSet(key: String, defValue: Set<String>): MutableSet<String> {
            return getPreferences().getStringSet(key,defValue) as MutableSet<String>
        }

        fun clearAllData() {
            val editor = getPreferences().edit()
            editor.clear()
            editor.commit()
        }

        fun getFavoriteKey(list: ArrayList<UserRepo>): String {
            if (list.isNotEmpty()) {
                return "${Preferences.Keys.USER_FAV_LIST}_${list[0].owner?.login ?: ""}"
            }
            return ""
        }

        fun getFavoriteKey(repo: UserRepo): String {
            return "${Preferences.Keys.USER_FAV_LIST}_${repo.owner?.login ?: ""}"
        }
    }
}