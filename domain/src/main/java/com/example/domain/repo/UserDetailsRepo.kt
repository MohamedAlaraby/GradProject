package com.example.domain.repo

import com.example.domain.entity.CategoryResponse
import com.example.domain.entity.User

interface UserDetailsRepo {
    suspend fun getUserDetailsFromRemote(): User
}