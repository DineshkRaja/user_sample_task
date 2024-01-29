package com.example.mydatasensetask.localDatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mydatasensetask.view.model.Users

@Dao
interface UsersDAO {
    @Query("SELECT * FROM users ORDER BY Id DESC")
    fun getUsersListLive(): LiveData<List<Users>>

    @Query("SELECT * FROM users ORDER BY Id DESC")
    suspend fun getUsersList(): List<Users>

    @Update
    suspend fun updateUser(users: Users)

    @Delete
    suspend fun deleteUser(users: Users)

    @Insert
    suspend fun insertUser(users: Users)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: Int): Users
}