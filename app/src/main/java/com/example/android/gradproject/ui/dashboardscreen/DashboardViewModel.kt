package com.example.android.gradproject.ui.dashboardscreen

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.example.android.gradproject.utils.Constants
import com.example.android.gradproject.utils.FireStoreClass
import com.example.domain.entity.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor
    ( private val  app:Application,
      private val  firebaseInstance:FirebaseFirestore,
      private val  fireStoreClass: FireStoreClass
    ):AndroidViewModel(app){
      private val _userDetails:MutableStateFlow<User?> =
            MutableStateFlow(null)
      val userDetails:StateFlow<User?> =_userDetails
    fun getUserDetails(){
        //here we pass the collection name from which we wants the data.
        firebaseInstance
            .collection(Constants.USERS)
            //THE DOCUMENT ID TO GET THE FIELDS FROM THE USER.
            .document(fireStoreClass.getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i("FireStoreClass ","get the user successfully!" )
                //here we have received the document snapShot which is converted into the user data model object.
                _userDetails.value= document.toObject(User::class.java)
                 Log.i("FireStoreClass ","name is ${_userDetails.value!!.fullName}" )
            }
            .addOnFailureListener { e ->
                Log.e("FireStoreClass ", "Error while getting the user details.", e)
            }
    }
}