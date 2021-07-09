package com.example.apitest

import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET(value = "/users")
    suspend fun getTodos(): Response<ArrayList<User>>
}