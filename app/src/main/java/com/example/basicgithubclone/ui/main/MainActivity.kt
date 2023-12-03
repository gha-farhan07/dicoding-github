package com.example.basicgithubclone.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicgithubclone.R
import com.example.basicgithubclone.adapter.GithubAdapter
import com.example.basicgithubclone.databinding.ActivityMainBinding
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.ui.favorite.FavoriteActivity
import com.example.basicgithubclone.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var searchUserAdapter: GithubAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
//
        setupSearchView()

        observeUserList()

        viewModel.getUser("q")

        binding.apply {
            searchBarer.inflateMenu(R.menu.search_menu)

            searchBarer.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite_menu -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
        }


    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    /**
     * Configures the RecyclerView's layout manager and adapter.
     */
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            searchUserAdapter = GithubAdapter() // Pass the binding to the adapter
            adapter = searchUserAdapter
        }
    }

    /**
     * Configures the search view to handle user input and initiate search.
     */
    private fun setupSearchView() {

        with(binding) {
            searchView.setupWithSearchBar(searchBarer)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBarer.setText(searchView.text)
                    searchView.hide()
                    showLoading(true)
                    viewModel.getUser(searchBarer.text.toString())
//                    showLoading(false)
                    true
                }
        }

    }

    /**
     * Observes the user list from the ViewModel and updates the adapter.
     */
    private fun observeUserList() {
        viewModel.liveUser.observe(this) { users ->
            users?.let { success ->
                searchUserAdapter.submitList(success as ArrayList<PersonItemList>)
                showLoading(false)
            }
        }
    }
}