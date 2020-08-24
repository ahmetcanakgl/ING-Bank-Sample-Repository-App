package co.aca.ingrepo.domain.network

import co.aca.ingrepo.domain.base.BaseUseCase
import co.aca.ingrepo.repository.Repository
import co.aca.ingrepo.repository.response.UserRepo
import co.aca.ingrepo.util.Preferences
import javax.inject.Inject

class GetUserRepoList @Inject constructor(private val repository: Repository) :
    BaseUseCase<ArrayList<UserRepo>, GetUserRepoList.Params>() {

    var response = ArrayList<UserRepo>()

    class Params(val username: String)

    override fun execute(params: Params) {
        sendRequest {
            repository.getUserRepoList(params.username)
        }
    }

    override fun onSendSuccess(t: ArrayList<UserRepo>) {

        var favoriteList = Preferences.getStringSet(
            Preferences.getFavoriteKey(t),
            setOf("0")
        ).toList() as? ArrayList<String>


        if (favoriteList != null) {
            for (i in 0 until t.size) {
                for (j in 0 until favoriteList.size) {
                    if (favoriteList[j] == (t[i].id).toString()) {
                        t[i].isAddedToFav = true
                    }
                }
            }
        }

        super.onSendSuccess(t)
    }

    override fun isLoadingEnable(): Boolean {
        return true
    }
}