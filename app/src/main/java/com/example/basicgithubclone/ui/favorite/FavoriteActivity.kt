package com.example.basicgithubclone.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicgithubclone.R
import com.example.basicgithubclone.adapter.GithubAdapter
import com.example.basicgithubclone.databinding.ActivityFavoriteBinding
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.viewmodel.FavoriteViewModel
import com.example.basicgithubclone.viewmodel.FollowViewModel
import com.example.basicgithubclone.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: GithubAdapter
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using the binding object and set it as the content view
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecylerView()

        viewModel.getFavoriteUser().observe(this) { users ->
            val items = arrayListOf<PersonItemList>()
            users.map {
                val item =
                    it.avatarUrl?.let { it1 -> PersonItemList(login = it.username, avatarUrl = it1, it.id) }
                if (item != null) {
                    items.add(item)
                }
            }
            adapter.submitList(items)
        }


    }


    private fun setUpRecylerView() {
        adapter = GithubAdapter()
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.setHasFixedSize(true)
    }
}