package com.example.mydatasensetask.view.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    var Id: Int,
    var userName: String,
    var age: Int,
    var dateOfBirth: String,
    var gender: String,
    var education: String
)