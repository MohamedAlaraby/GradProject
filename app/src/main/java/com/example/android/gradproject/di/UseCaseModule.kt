package com.example.android.gradproject.di

import com.example.domain.repo.MealsRepo
import com.example.domain.repo.UserDetailsRepo
import com.example.domain.usecase.GetMealsUseCase
import com.example.domain.usecase.GetUserDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideGetMealsUseCase(mealsRepo: MealsRepo):GetMealsUseCase{
        return GetMealsUseCase(mealsRepo)
    }
    @Singleton
    @Provides
    fun provideUserDetailsUseCase(userDetailsRepo: UserDetailsRepo):GetUserDetailsUseCase{
        return GetUserDetailsUseCase(userDetailsRepo)
    }


}