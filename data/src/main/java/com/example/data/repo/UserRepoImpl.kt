package com.example.data.repo

import com.example.data.remote.ApiService
import com.example.domain.entity.User
import com.example.domain.repo.UserDetailsRepo

class UserRepoImpl (
    private val apiService: ApiService
        ):UserDetailsRepo{
    override suspend fun getUserDetailsFromRemote(): User {
        return apiService.getUserDetails()
    }

}