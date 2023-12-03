package com.example.basicgithubclone.database.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basicgithubclone.database.dao.NoteDao
import com.example.basicgithubclone.database.entity.FavoriteUser

@Database(
    entities = [FavoriteUser::class], // Tell the database the entries will hold data of this type
    version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun getDao(): NoteDao
}