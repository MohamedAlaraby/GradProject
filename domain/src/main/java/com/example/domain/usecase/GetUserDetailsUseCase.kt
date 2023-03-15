package com.example.domain.usecase

import com.example.domain.repo.UserDetailsRepo

class GetUserDetailsUseCase(val repo : UserDetailsRepo) {
    suspend operator fun invoke() = repo.getUserDetailsFromRemote()
}