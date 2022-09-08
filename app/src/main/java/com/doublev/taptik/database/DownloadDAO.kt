package com.doublev.taptik.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DownloadDAO {
    @Insert
    fun insert(downloadData: DownloadData)

    @Query("SELECT * FROM download_data")
    fun getListDownloadData():List<DownloadData>
}