package com.example.domain.usecase

import com.example.domain.repo.MealsRepo

class GetMealsUseCase (val mealsRepo:MealsRepo){
   suspend operator fun invoke() = mealsRepo.getMealsFromRemote()
}