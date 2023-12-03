package com.example.basicgithubclone.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.basicgithubclone.R
import com.example.basicgithubclone.adapter.SectionPageAdapter
import com.example.basicgithubclone.database.databse.FavoriteDatabase
import com.example.basicgithubclone.databinding.ActivityDetailBinding
import com.example.basicgithubclone.ui.detail.followerfragment.FollowFragment
import com.example.basicgithubclone.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarProfile = intent.getStringExtra(EXTRA_AVATAR)
        if (username != null) {
            getUserDetail(username)
        }

        val fragment = FollowFragment()
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        fragment.arguments = bundle

        setUserDetail()
        if (username != null) {
            setFollowerFollowingFragmentSection(username)
        }

        if (username != null) {
            if (avatarProfile != null) {
                setUpFab(username, id, avatarProfile)
            }
        }


    }

    private fun setUpFab(username: String, id: Int, avatarUrl: String) {
        var isFavorite = false

        // Check if the user is already a favorite
        lifecycleScope.launch {
            isFavorite = viewModel.checkUser(username) != null
        }
        // Set the initial icon based on whether the user is a favorite
        binding.fab.setImageDrawable(
            if (isFavorite) {
                resources.getDrawable(R.drawable.outline_favorite_border_24)
            } else {
                resources.getDrawable(R.drawable.full_favorite_24)
            }
        )

        // Set up click listener
        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                if (isFavorite) {
                    // User is already a favorite, remove from favorites
//                        viewModel.removeFavoriteUser(username)
                    lifecycleScope.launch {
                        viewModel.removeFavoriteUser(id)
                    }

                    binding.fab.setImageDrawable(
                        resources.getDrawable(R.drawable.outline_favorite_border_24)
                    )
                } else {
                    // User is not a favorite, add to favorites
                    viewModel.insertFavoriteUser(username, id, avatarUrl)
                    binding.fab.setImageDrawable(
                        resources.getDrawable(R.drawable.full_favorite_24)
                    )
                }
                // Toggle the favorite status
                isFavorite = !isFavorite
            }
        }
    }


//        binding.fab.setOnClickListener { view ->
//            val message = if (!fabVisible) {
//
//                fabVisible = true
//                "Here's a Snackbar Add"
//
//            } else {
//
//                fabVisible = false
//                "Here's a Snackbar Delete"
//            }
//
//            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .show()
//
//
//
//        }


private fun setFollowerFollowingFragmentSection(username: String) {

    val sectionsPagerAdapter = SectionPageAdapter(this)

    sectionsPagerAdapter.setUsersname(username)

    val viewPager: ViewPager2 = findViewById(R.id.view_pager)
    viewPager.adapter = sectionsPagerAdapter
    val tabs: TabLayout = findViewById(R.id.tabs)
    TabLayoutMediator(tabs, viewPager) { tab, position ->
        tab.text = resources.getString(TAB_TITLES[position])

    }.attach()
}

private fun setUserDetail() {
    viewModel.detailUser.observe(this) {
        it.let { detailUser ->
            if (detailUser != null) {
                binding.apply {
                    tvNameDetail.text = detailUser.login
                    Glide.with(this@DetailActivity).load(detailUser.avatarUrl)
                        .circleCrop().into(ivProfile)
                    FollowersTv.text = "${detailUser.followers} Followers"
                    FollowingTv.text = "${detailUser.following} Following"
                }
            }
        }
    }
}

private fun getUserDetail(username: String) {
    viewModel.getUserDetail(username)
}


companion object {
    const val EXTRA_USERNAME = "extra_username"
    const val EXTRA_ID = "extra_id"
    const val EXTRA_AVATAR = "extra_avatar"

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )
}


}