package com.example.lab9

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface JsonPlaceholderAPI {
    companion object {
        const val BASE_URL = "http://tgryl.pl/shoutbox/"
    }

    @GET("messages")
    fun getMessages(): Call<List<Message>>

    @POST("message")
    fun postMessage(login: String,content: String)

    @PUT("message/{id}")
    fun editMessage(login: String,content: String)

    @DELETE("message/{id}")
    fun deleteMessage(id: String)

}