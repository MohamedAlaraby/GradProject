package com.example.android.gradproject.ui.loginscreen

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.gradproject.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInViewModel( private val app:Application,
                       private val listener: OnSignInStartedListener
                      ):AndroidViewModel(app) {
    private var auth: FirebaseAuth = Firebase.auth
    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: MutableLiveData<FirebaseUser?> = _currentUser

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    private val googleSignInClient = GoogleSignIn.getClient(app, gso)

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = auth.currentUser
            } else {
                _currentUser.value = null
            }
        }
    }
    fun signIn() {
        listener.onSignInStarted(googleSignInClient)
    }




}