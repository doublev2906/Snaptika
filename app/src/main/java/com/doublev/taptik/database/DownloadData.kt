package com.doublev.taptik.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_data")
data class DownloadData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String?,
    @ColumnInfo(name = "app_name")
    val appName: String = "TIKTOK",
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "kind")
    val kind: String = "video",
)