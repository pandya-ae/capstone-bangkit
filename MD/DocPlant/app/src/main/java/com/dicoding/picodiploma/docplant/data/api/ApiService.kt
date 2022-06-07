package com.dicoding.picodiploma.docplant.data.api

import com.dicoding.picodiploma.docplant.model.LoginModel
import com.dicoding.picodiploma.docplant.model.RegisterModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterModel

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginModel
}