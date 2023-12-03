package com.example.basicgithubclone.ui.detail.followerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicgithubclone.R
import com.example.basicgithubclone.adapter.GithubAdapter
import com.example.basicgithubclone.databinding.FragmentFollowBinding
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.viewmodel.FollowViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FollowFragment : Fragment(R.layout.fragment_follow) {

    private val viewModel: FollowViewModel by viewModels()
    private lateinit var adapter: GithubAdapter
    private var _binding: FragmentFollowBinding? = null // Declare the binding property
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =
            FragmentFollowBinding.inflate(inflater, container, false) // Inflate the binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_SECTION_NUMBER)
        val username = arguments?.getString(ARG_USERNAME, "error") ?: "error"

        setUpRecylerView()
        if (index != null) {
            setUpFollow(index, username)
        }


    }

    private fun setUpFollow(index: Int, username: String) {
        if (index == 1) {
            viewModel.getFollower(username)
            viewModel.followerList.observe(viewLifecycleOwner) {
                adapter.submitList(it as ArrayList<PersonItemList>)
            }
        } else {
            viewModel.getFollowing(username)
            viewModel.followingList.observe(viewLifecycleOwner) {
                adapter.submitList(it as ArrayList<PersonItemList>)
            }
        }
    }


    private fun setUpRecylerView() {
        adapter = GithubAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.adapter = adapter
        binding.rvUser.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding when the view is destroyed
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "extra_username"
    }
}