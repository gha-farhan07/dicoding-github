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
class MainViewModel @Inject constructor(private val repository: MyRepository) : ViewModel() {
    private val _liveUser = MutableLiveData<List<PersonItemList>>()
    val liveUser: LiveData<List<PersonItemList>> get() = _liveUser

    fun getUser(query: String, page: Int = 1) {
        repository.getAllGithubPeople(query, page).enqueue(object : Callback<PersonResponse> {
            override fun onResponse(
                call: Call<PersonResponse>,
                response: Response<PersonResponse>
            ) {
                if (response.isSuccessful) {
                    val userList = response.body()?.items.orEmpty()
                    Log.d("MainViewModel", "User List size: ${userList.size}")

                    // Append the new list to the existing list
                    val currentList = _liveUser.value.orEmpty().toMutableList()
                    currentList.addAll(userList)
                    _liveUser.postValue(currentList)

                    // Check if there are more pages
                    val nextPage = page + 1
                    if (userList.size == PAGE_SIZE) {
                        // Fetch the next page
                        getUser(query, nextPage)
                    }
                } else {
                    Log.e("MainViewModel", "Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PersonResponse>, t: Throwable) {
                Log.e("MainViewModel", "Network request failed", t)
            }
        })
    }


//    fun getUser(query: String) {
//
//        repository.getAllGithubPeople(query, page).enqueue(object : Callback<PersonResponse> {
//            override fun onResponse(
//                call: Call<PersonResponse>,
//                response: Response<PersonResponse>
//            ) {
//                if (response.isSuccessful) {
//                    _liveUser.postValue(response.body()?.items.orEmpty()) // Use orEmpty() to handle null case
//                } else {
//                    Log.e("MainViewModel", "Response not successful: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PersonResponse>, t: Throwable) {
//                Log.e("MainViewModel", "Network request failed", t)
//            }
//        })
//
//
//    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}