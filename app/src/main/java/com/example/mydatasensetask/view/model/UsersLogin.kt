package com.example.mydatasensetask.view.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_logins")
data class UsersLogin(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int = 0,
    var userName: String,
    var email: String,
    var password: String
)