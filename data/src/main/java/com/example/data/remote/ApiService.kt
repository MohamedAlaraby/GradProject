package com.example.data.remote

import com.example.domain.entity.CategoryResponse
import com.example.domain.entity.User
import retrofit2.http.GET

interface ApiService{
   @GET("categories.php")
   suspend fun getMeals():CategoryResponse
   suspend fun getUserDetails():User
}