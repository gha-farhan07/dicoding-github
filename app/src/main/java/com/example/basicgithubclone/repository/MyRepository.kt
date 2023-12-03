package com.example.basicgithubclone.repository

import com.example.basicgithubclone.database.dao.NoteDao
import com.example.basicgithubclone.database.entity.FavoriteUser
import com.example.basicgithubclone.remote.api.ApiService
import javax.inject.Inject

class MyRepository @Inject constructor(
    private val apiService: ApiService, private val favDao: NoteDao
) {

    fun getAllGithubPeople(query: String, page: Int) = apiService.getGithubAccount(query, page)
    fun getUserDetail(username: String) = apiService.getUsername(username)
    fun getFollower(query: String, page: Int) = apiService.getFollower(query, page)
    fun getFollowing(query: String, page: Int) = apiService.getFollowing(query, page)
    suspend fun insertItem(entities: FavoriteUser) = favDao.insert(entities)
    fun getFavUserByUsername(username: String) = favDao.getFavoriteUserByUsername(username)
    suspend fun removeFromFavorite(entities: Int) = favDao.removeFromFavorite(entities)
    fun getUserFavoriteAll() = favDao.getFavoriteUser()

}