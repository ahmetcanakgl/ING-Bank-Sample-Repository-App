package co.aca.ingrepo.ui.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import co.aca.ingrepo.base.BaseViewModel
import co.aca.ingrepo.repository.response.UserRepo
import co.aca.ingrepo.util.Preferences
import co.aca.ingrepo.util.Preferences.Companion.getFavoriteKey
import co.aca.ingrepo.util.Preferences.Companion.getStringSet
import javax.inject.Inject

class FRRepoViewModel @Inject constructor() : BaseViewModel() {

    var userRepo: UserRepo? = null

    var favoriteList: MutableSet<String>? = null

    lateinit var onUserRepoReceived: MutableLiveData<UserRepo>

    override fun onCreate(savedInstanceState: Bundle?, arguments: Bundle?) {
        super.onCreate(savedInstanceState, arguments)

        onUserRepoReceived = MutableLiveData()

        if (arguments != null) {
            userRepo = arguments.getSerializable("repoModel") as UserRepo?

            if (userRepo != null) {
                onUserRepoReceived.postValue(userRepo)
                favoriteList = getStringSet(getFavoriteKey(userRepo!!), setOf("0")).toMutableSet()
            }
        }
    }

    fun favoriteButtonClicked() {


        userRepo!!.isAddedToFav = !userRepo!!.isAddedToFav

        if (userRepo!!.isAddedToFav) {
            favoriteList?.add(userRepo!!.id.toString())
        } else {
            favoriteList?.remove(userRepo!!.id.toString())
        }

        Preferences.setStringSet(getFavoriteKey(userRepo!!), favoriteList)

        onUserRepoReceived.postValue(userRepo)
    }
}