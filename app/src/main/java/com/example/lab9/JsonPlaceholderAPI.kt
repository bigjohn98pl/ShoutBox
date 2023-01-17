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
    fun postMessage(@Body body: AddMessage): Call<Message>

    @FormUrlEncoded
    @PUT("/shoutbox/message/{id}")
    fun editMessage(@Path("id") id:String, @Field("login") login:String, @Field("content") content:String): Call<Void>

    @DELETE("/shoutbox/message/{id}")
    fun deleteMessage(@Path("id") id:String): Call<Void>

}