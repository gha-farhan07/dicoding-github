package com.example.basicgithubclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.remote.response.PersonResponse
import com.example.basicgithubclone.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class FollowViewModel @Inject constructor(private val repository: MyRepository) : ViewModel() {


    private val _followerList = MutableLiveData<List<PersonItemList>>()
    val followerList: LiveData<List<PersonItemList>> get() = _followerList


    private val _followingList = MutableLiveData<List<PersonItemList>>()
    val followingList: LiveData<List<PersonItemList>> get() = _followingList


    fun getFollower(query: String, page: Int = 1) {
        repository.getFollower(query, page).enqueue(object : Callback<List<PersonItemList>> {
            override fun onResponse(
                call: Call<List<PersonItemList>>,
                response: Response<List<PersonItemList>>
            ) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    Log.d("MainViewModel", "User List size: ${userList?.size}")

                    // Append the new list to the existing list
                    val currentList = _followerList.value.orEmpty().toMutableList()
                    if (userList != null) {
                        currentList.addAll(userList)
                    }

                    _followerList.postValue(currentList)

                    // Check if there are more pages
                    val nextPage = page + 1
                    if (userList != null) {
                        if (userList.size == FollowViewModel.PAGE_SIZE) {
                            // Fetch the next page
                           getFollower(query, nextPage)
                        }
                    }
                } else {
                    Log.e("MainViewModel", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<PersonItemList>>, t: Throwable) {
                Log.e("MainViewModel", "Network request failed", t)
            }


        })
    }


    fun getFollowing(query: String, page: Int = 1) {
        repository.getFollowing(query, page).enqueue(object : Callback<List<PersonItemList>> {
            override fun onResponse(
                call: Call<List<PersonItemList>>,
                response: Response<List<PersonItemList>>
            ) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    Log.d("MainViewModel", "User List size: ${userList?.size}")

                    // Append the new list to the existing list
                    val currentList = _followingList.value.orEmpty().toMutableList()
                    userList?.let { currentList.addAll(it) }
                    _followingList.postValue(currentList)

                    // Check if there are more pages
                    val nextPage = page + 1
                    if (userList?.size == FollowViewModel.PAGE_SIZE) {
                        // Fetch the next page
                        getFollowing(query, nextPage)
                    }
                } else {
                    Log.e("MainViewModel", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<PersonItemList>>, t: Throwable) {
                Log.e("MainViewModel", "Network request failed", t)
            }


        })

    }

    companion object {
        private const val PAGE_SIZE = 30
    }

}
