package com.doublev.taptik.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides

@Module
class DownloadModule(private val context: Context) {
    @Provides fun providesDownloadContext() = context
    @Provides fun providesDownloadDatabase(context: Context) : DownloadDatabase =
        Room.databaseBuilder(context,DownloadDatabase::class.java,"downloadDb").allowMainThreadQueries().build()

}