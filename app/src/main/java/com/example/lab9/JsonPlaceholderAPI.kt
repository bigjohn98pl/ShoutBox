package com.example.lab9

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceholderAPI {
    companion object {
        const val BASE_URL = "https://tgryl.pl"
    }

    @GET("/shoutbox/messages")
    fun getMessages(): Call<ArrayList<Message>>

    @POST("/shoutbox/message")
    fun postMessage(login: String,content: String): Call<Message>

    @PUT("/shoutbox/message/{id}")
    fun editMessage(login: String,content: String): Call<Void>

    @DELETE("/shoutbox/message/{id}")
    fun deleteMessage(@Path("id") id:String): Call<Void>

}