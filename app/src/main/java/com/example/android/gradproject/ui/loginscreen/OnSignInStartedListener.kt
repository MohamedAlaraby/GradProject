package com.example.android.gradproject.ui.loginscreen

import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface OnSignInStartedListener {
    fun onSignInStarted(client: GoogleSignInClient?)
}