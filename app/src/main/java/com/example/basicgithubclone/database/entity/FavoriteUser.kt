package com.example.basicgithubclone.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String = "",
    var avatarUrl: String? = null,
)