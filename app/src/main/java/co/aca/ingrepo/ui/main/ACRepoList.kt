package co.aca.ingrepo.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import co.aca.ingrepo.R
import co.aca.ingrepo.base.BaseActivity
import co.aca.ingrepo.repository.response.UserRepo
import co.aca.ingrepo.util.Util
import co.aca.ingrepo.util.extensions.showToast
import kotlinx.android.synthetic.main.ac_repo_list.*

class ACRepoList : BaseActivity<ACRepoListViewModel>() {

    private lateinit var gridLayoutManager: GridLayoutManager
    var adapter: RepoListAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.ac_repo_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        gridLayoutManager = GridLayoutManager(this, 1)
        super.onCreate(savedInstanceState)

        recyclerView.layoutManager = gridLayoutManager

    }

    override fun setListeners() {
        btnSubmit.setOnClickListener {
            if (validateInput()) {
                viewModel.getRepoList(etSearch.text.toString())
            }
        }
    }

    private fun validateInput(): Boolean {
        if (etSearch.text.toString().isEmpty()) {
            showToast("Please enter the repo username.", isSuccess = false)
            return false
        }
        return true
    }

    override fun setReceivers() {

        viewModel.onUserRepoListFetched.observe(this, Observer {
            if (!it.isNullOrEmpty()) {

                val index = gridLayoutManager.findFirstVisibleItemPosition()
                val v = recyclerView.getChildAt(0)
                val top = if (v == null) 0 else v.top - recyclerView.paddingTop

                setAdapter(it)

                gridLayoutManager.scrollToPositionWithOffset(index, top)
            }else{

            }
        })
    }

    private fun setAdapter(list: ArrayList<UserRepo>) {
        adapter = object : RepoListAdapter(this, list) {
            override fun onRepoClicked(repo: UserRepo, position: Int) {
                Util.hideKeyboard(this@ACRepoList)
                viewModel.goToDetailPage(repo)
            }
        }
        recyclerView.adapter = adapter
    }
}
