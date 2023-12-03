package com.example.basicgithubclone.remote.api

import com.example.basicgithubclone.remote.response.DetailResponse
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.remote.response.PersonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getGithubAccount(@Query("q") query: String, @Query("page") page: Int): Call<PersonResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUsername(@Path("username") username: String): Call<DetailResponse>


    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollower(@Path("username") username: String, @Query("page") page: Int): Call<List<PersonItemList>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String, @Query("page") page: Int): Call<List<PersonItemList>>


}