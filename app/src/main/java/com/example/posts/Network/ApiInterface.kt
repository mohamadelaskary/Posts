package com.exampel.posts.Network

import com.exampel.posts.Model.Post
import dagger.Component
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

interface ApiInterface {
    @GET("posts")
    suspend fun getPosts() : Response<List<Post>>
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int) : Response<Post>
}