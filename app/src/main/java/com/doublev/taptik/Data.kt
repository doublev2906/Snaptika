package com.doublev.taptik

data class Data(
    val id:String,
    val title:String,
    val origin_cover:String,
    val play:String,
    val wmplay:String,
    val music:String,
    val author: Author
)
data class Author(val nickname: String)
data class TikTokData(val data:Data)