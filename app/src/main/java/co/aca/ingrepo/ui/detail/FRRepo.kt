package co.aca.ingrepo.ui.detail

import android.os.Bundle
import co.aca.ingrepo.R
import co.aca.ingrepo.base.BaseFragment
import co.aca.ingrepo.repository.response.UserRepo
import co.aca.ingrepo.util.DateUtils
import co.aca.ingrepo.util.extensions.observe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fr_repo.*

class FRRepo : BaseFragment<FRRepoViewModel>() {

    companion object {

        fun newInstance(userRep: UserRepo): FRRepo {

            val data = Bundle()
            data.putSerializable("repoModel", userRep)

            val fragment = FRRepo()
            fragment.arguments = data

            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fr_repo
    }

    override fun setListeners() {

        imBack.setOnClickListener { activity!!.onBackPressed() }
        ivStars.setOnClickListener { viewModel.favoriteButtonClicked() }
    }

    override fun setReceivers() {

        observe(viewModel.onUserRepoReceived, { userRepo: UserRepo? ->

            if (!userRepo?.owner?.avatar_url.isNullOrEmpty()) {
                Picasso.get().load(userRepo?.owner?.avatar_url).into(ivUserPic)
            }

            tvTitle.text = userRepo!!.name
            tvStatistic.text = getString(R.string.statistics_for, userRepo.name)
            tvOwner.text = userRepo.owner?.login
            tvStars.text = userRepo.stargazers_count.toString()
            tvOpenIssues.text = userRepo.open_issues_count.toString()
            tvLastUpdated.text = DateUtils.changeDateFormat(userRepo.updated_at!!,"yyyy-MM-dd'T'HH:mm:ss'Z'","dd.MM.yyyy")

            if (userRepo.isAddedToFav) {
                ivStars.setImageDrawable(requireContext().getDrawable(R.drawable.star_filled))
            } else {
                ivStars.setImageDrawable(requireContext().getDrawable(R.drawable.star_stroked))
            }
        })

    }

}
