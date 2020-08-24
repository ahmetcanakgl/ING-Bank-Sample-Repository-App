package co.aca.ingrepo.repository

import com.google.gson.Gson
import javax.inject.Inject

class UserRepository @Inject constructor(private val gson: Gson) {
//    /** -------------------------------------------------------------------------------
//     * USER REGION
//     * ------------------------------------------------------------------------------- */
//    private var user: LoginModel? = null
//    private var token: String? = null
//
//    fun getUser(): LoginModel? {
//        if (user == null) {
//            user = getUserFromPref()
//        }
//
//        return user
//    }
//
//    fun setUser(user: LoginModel?) {
//        this.user = user
//
//        if (user == null) {
//            deleteUserFromPref()
//        } else {
//            saveUserToPref()
//        }
//    }
//
//    fun getToken(): String? {
//        if (token == null) {
//            token = getTokenFromPref()
//        }
//
//        return token
//    }
//
//    fun setToken(token: String?) {
//        this.token = token
//
//        if (token == null) {
//            deleteTokenFromPref()
//        } else {
//            saveTokenToPref()
//        }
//    }
//
//    fun isLogged(): Boolean {
//        return getUser() != null
//    }
//
//    /**
//     * User preferences
//     */
//    private fun getUserFromPref(): LoginModel? {
//        var json = Preferences.getString(Preferences.Keys.USER)
//
//        if (TextUtils.isEmpty(json)) return null
//
//        return gson.fromJson(Preferences.getString(Preferences.Keys.USER), LoginModel::class.java)
//    }
//
//    private fun deleteUserFromPref() {
//        Preferences.deleteKey(Preferences.Keys.USER)
//    }
//
//    private fun saveUserToPref() {
//        Preferences.setString(Preferences.Keys.USER, gson.toJson(user))
//    }
//
//    private fun getTokenFromPref(): String? {
//        return Preferences.getString(Preferences.Keys.TOKEN)
//    }
//
//    private fun deleteTokenFromPref() {
//        Preferences.deleteKey(Preferences.Keys.TOKEN)
//    }
//
//    private fun saveTokenToPref() {
//        Preferences.setString(Preferences.Keys.TOKEN, token!!)
//    }
}