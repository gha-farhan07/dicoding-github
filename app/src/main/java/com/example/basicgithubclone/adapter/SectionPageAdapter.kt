package com.example.basicgithubclone.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.basicgithubclone.R
import com.example.basicgithubclone.ui.detail.followerfragment.FollowFragment

class SectionPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    // Fungsi untuk mengatur nama pengguna
    fun setUsersname(username: String) {
        this.username = username
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_SECTION_NUMBER, position + 1) // Menambahkan argumen posisi
            putString(FollowFragment.ARG_USERNAME, username) // Menambahkan argumen nama pengguna
        }
        return fragment
    }
}