package com.example.basicgithubclone.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basicgithubclone.database.entity.FavoriteUser

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: FavoriteUser)

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Query("DELETE FROM FavoriteUser WHERE id = :id")
    suspend fun removeFromFavorite(id: Int): Int

    @Query("SELECT * FROM FavoriteUser")
    fun getFavoriteUser() : LiveData<List<FavoriteUser>>
}
