package com.example.mydatasensetask.dependencyInjection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.mydatasensetask.localDatabase.SenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesDataBase(application: Application): SenseDatabase {
        return SenseDatabase.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("SENSE", Context.MODE_PRIVATE)
    }

}