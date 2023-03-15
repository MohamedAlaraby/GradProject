package com.example.android.gradproject.di

import com.example.data.remote.ApiService
import com.example.data.repo.MealsRepoImpl
import com.example.data.repo.UserRepoImpl
import com.example.domain.repo.MealsRepo
import com.example.domain.repo.UserDetailsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

        @Provides
         fun provideRepo(apiService: ApiService): MealsRepo {
            return MealsRepoImpl(apiService = apiService)
        }
    @Provides
    fun provideUserRepo(apiService: ApiService): UserDetailsRepo {
        return UserRepoImpl(apiService = apiService)
    }

}