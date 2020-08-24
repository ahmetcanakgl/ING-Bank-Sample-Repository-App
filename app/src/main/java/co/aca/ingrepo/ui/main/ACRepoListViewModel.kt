package co.aca.ingrepo.ui.main

import androidx.lifecycle.MutableLiveData
import co.aca.ingrepo.base.BaseViewModel
import co.aca.ingrepo.domain.network.GetUserRepoList
import co.aca.ingrepo.repository.response.UserRepo
import co.aca.ingrepo.ui.detail.FRRepo
import co.aca.ingrepo.util.extensions.navigateToFragment
import javax.inject.Inject

class ACRepoListViewModel @Inject constructor(val getUserRepoList: GetUserRepoList) :
    BaseViewModel(getUserRepoList) {

    var onUserRepoListFetched = MutableLiveData<ArrayList<UserRepo>>()

    fun getRepoList(username: String) {

        getUserRepoList.execute(
            GetUserRepoList.Params(username)
        ) { arrayList, errorModel ->
            if (arrayList != null) {
                onUserRepoListFetched.postValue(arrayList)
            }else{
                viewListener?.showToast(errorModel?.message ?: "Username not found", false)
            }
        }
    }

    fun goToDetailPage(owner: UserRepo) {
        navigateToFragment(FRRepo.newInstance(owner))
    }
}