package com.example.basicgithubclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicgithubclone.database.databse.FavoriteDatabase
import com.example.basicgithubclone.database.entity.FavoriteUser
import com.example.basicgithubclone.remote.response.DetailResponse
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.remote.response.PersonResponse
import com.example.basicgithubclone.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: MyRepository,
    private val db: FavoriteDatabase) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser: LiveData<DetailResponse> get() = _detailUser


    init {
        // Initialize your database here if needed
        db.getDao()
    }

    fun getUserDetail(username: String) {
        repository.getUserDetail(username).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    val detailUsers = response.body()
                    _detailUser.postValue(detailUsers!!)
                } else {
                    if (response.code() == 404) {
                        // Handle 404 error (user not found)
                        Log.e("DetailViewModel", "User not found")
                        // You can set a default or placeholder user here if needed
                    } else {
                        // Handle other response codes
                        Log.e("DetailViewModel", "Response not successful: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                // Handle network request failure
                Log.e("DetailViewModel", "Network request failed", t)
            }
        })
    }

    fun insertFavoriteUser(username: String, id: Int, avatarUrl: String) {
        viewModelScope.launch {
            var user = FavoriteUser(id, username, avatarUrl)
            repository.insertItem(user)
        }
    }

    fun removeFavoriteUser(username: Int) {
        viewModelScope.launch {
            repository.removeFromFavorite(username)
        }
    }

    fun checkUser(username: String) = repository.getFavUserByUsername(username)



}