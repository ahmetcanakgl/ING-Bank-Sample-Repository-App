package co.aca.ingrepo.repository

import co.aca.ingrepo.repository.response.UserRepo
import io.reactivex.Single
import javax.inject.Inject

class Repository @Inject constructor(private var apiService: ApiService) {

    fun getUserRepoList(username: String): Single<ArrayList<UserRepo>> {
        return apiService.getUserRepoList(username = username)
    }
}