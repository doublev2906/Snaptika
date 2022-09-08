package com.doublev.taptik

import retrofit2.Call
import retrofit2.http.*

interface TiktokService {
//    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("mobile")
    fun getData(@FieldMap params:Map<String,String>):Call<TikTokData>

}