package co.aca.ingrepo.repository

import co.aca.ingrepo.repository.response.UserRepo
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @GET("/users/{username}/repos")
    fun getUserRepoList(@Path("username") username: String): Single<ArrayList<UserRepo>>

}