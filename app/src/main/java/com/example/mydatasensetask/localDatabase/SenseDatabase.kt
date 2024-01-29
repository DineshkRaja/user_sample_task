package com.example.mydatasensetask.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mydatasensetask.localDatabase.dao.UsersDAO
import com.example.mydatasensetask.localDatabase.dao.UsersLoginDAO
import com.example.mydatasensetask.view.model.Users
import com.example.mydatasensetask.view.model.UsersLogin

@Database(entities = [UsersLogin::class, Users::class], version = 1, exportSchema = false)
abstract class SenseDatabase : RoomDatabase() {
    abstract fun getUsersLoginDao(): UsersLoginDAO
    abstract fun getUsersDao(): UsersDAO

    companion object {
        @Volatile
        private var INSTANCE: SenseDatabase? = null
        fun getDatabase(context: Context): SenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SenseDatabase::class.java,
                    "sense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}