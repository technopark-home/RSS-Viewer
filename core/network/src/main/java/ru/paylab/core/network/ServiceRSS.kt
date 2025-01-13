package ru.paylab.core.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ServiceRSS {
    @GET
    fun getFeed(@Url url: String ): Call<ResponseBody?>?
}
