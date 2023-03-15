package com.example.android.gradproject.ui.loginscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SignInViewModelFactory(
    private val app:Application,
    private val listener: OnSignInStartedListener
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(app, listener) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}