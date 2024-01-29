package com.example.mydatasensetask.localDatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mydatasensetask.view.model.UsersLogin

@Dao
interface UsersLoginDAO {

    @Query("SELECT * FROM user_logins")
    fun getAllUserLogins(): List<UsersLogin>

    @Query("SELECT * FROM user_logins WHERE userName = :username AND password = :password")
    fun getUserLogins(username: String, password: String): UsersLogin?

    @Query("SELECT * FROM user_logins WHERE email = :email AND password = :password")
    fun getUserLoginsMail(email: String, password: String): UsersLogin?

    @Insert
    fun newUserLogin(usersLogin: UsersLogin)

    @Update
    fun updateUserLogin(usersLogin: UsersLogin)

    @Delete
    fun deleteUserLogin(usersLogin: UsersLogin)

}