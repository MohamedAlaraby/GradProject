package com.example.android.gradproject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.CategoryResponse
import com.example.domain.usecase.GetMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealsViewModel @Inject constructor
    (private val mealsUseCase: GetMealsUseCase)
    :ViewModel() {
    private val _categories:MutableStateFlow<CategoryResponse?> = MutableStateFlow(null)
    val categories:StateFlow<CategoryResponse?> = _categories
    fun getMeals(){
        viewModelScope.launch {
            try {
                _categories.value =mealsUseCase()
            }catch (e:Exception){
                Log.e("meals view model",e.message.toString())
            }

        }

    }

}