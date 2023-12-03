package com.example.basicgithubclone.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.basicgithubclone.database.dao.NoteDao
import com.example.basicgithubclone.database.databse.FavoriteDatabase
import com.example.basicgithubclone.database.entity.FavoriteUser
import com.example.basicgithubclone.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: MyRepository
, private val dbDatabase: FavoriteDatabase, dao: NoteDao) : ViewModel() {

    init {
        dao.getFavoriteUser()
        dbDatabase.getDao()
    }


    fun getFavoriteUser() : LiveData<List<FavoriteUser>> {
        return repository.getUserFavoriteAll()
    }
}