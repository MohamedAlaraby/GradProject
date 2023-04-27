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
    private var userName = ""
    private var userEmail = ""
    private lateinit var userImageUrl: String
    fun getUserInformation(){
        //get the user details if he signed in with google
        val acct = GoogleSignIn.getLastSignedInAccount(app.applicationContext)
        if (acct != null) {
            //the user logged in using google account.
            firebaseInstance
                .collection(Constants.USERS)
                .document(acct.id!!)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // You can check here if document exist.
                        // It will be empty if it doesnt.
                        val isExist = task.result.exists()
                        if (isExist) {
                            getUserDetails()
                            Log.d("DashboardViewModel","The document is exist")
                        } else {
                            Log.d("DashboardViewModel","The document is not exist,now you can register it")
                            //sign in with google for the first time
                            userImageUrl =  acct.photoUrl.toString()
                            userName     =  acct.displayName.toString()
                            userEmail    =  acct.email.toString()
                            val user=User(
                                id=fireStoreClass.getCurrentUserID(),
                                image =userImageUrl,
                                fullName = userName,
                                email = userEmail
                            )

                            _userDetails.value=user
                            fireStoreClass.registerUser(userInfo = user)
                        }
                    }
                }
        }
        else {
            //the user is logged in using email and password.
            getUserDetails()
        }
    }
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