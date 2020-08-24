package co.aca.ingrepo.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.aca.ingrepo.R
import co.aca.ingrepo.repository.response.UserRepo
import co.aca.ingrepo.util.Preferences
import co.aca.ingrepo.util.Preferences.Companion.getFavoriteKey

abstract class RepoListAdapter(var context: Context, val userRepoList: ArrayList<UserRepo>) :
    RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    private var favoriteList: MutableSet<String>?

    init {
        favoriteList =
            Preferences.getStringSet(getFavoriteKey(userRepoList), setOf("0")).toMutableSet()

    }

    abstract fun onRepoClicked(repo: UserRepo, position: Int)

    override fun getItemCount(): Int {
        return userRepoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {

        return RepoViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.repo_item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val userRepo = userRepoList[position]

        with(holder) {
            tvRepoTitle.text = userRepo.name

            if (userRepo.isAddedToFav) {
                ivFavIcon.setImageDrawable(ivFavIcon.context.getDrawable(R.drawable.star_filled))
            } else {
                ivFavIcon.setImageDrawable(ivFavIcon.context.getDrawable(R.drawable.star_stroked))
            }

            ivFavIcon.setOnClickListener {
                userRepo.isAddedToFav = !userRepo.isAddedToFav

                if (userRepo.isAddedToFav) {
                    favoriteList?.add(userRepo.id.toString())
                } else {
                    favoriteList?.remove(userRepo.id.toString())
                }

                Preferences.setStringSet(getFavoriteKey(userRepoList), favoriteList)

                notifyDataSetChanged()
            }

            itemView.setOnClickListener {
                onRepoClicked(userRepo, position)
            }
        }
    }

    class RepoViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var tvRepoTitle: TextView = itemLayoutView.findViewById(R.id.tvRepoTitle)
        var ivFavIcon: ImageView = itemLayoutView.findViewById(R.id.ivFavIcon)
    }
}
