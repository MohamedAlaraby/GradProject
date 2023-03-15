package com.example.android.gradproject.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.android.gradproject.ui.DisplayProfileFragment
import com.example.android.gradproject.ui.EditProfileActivity
import com.example.android.gradproject.ui.RegisterActivity
import com.example.android.gradproject.ui.dashboardscreen.DashboardActivity
import com.example.domain.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Singleton

class FireStoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: Activity, userInfo: User) {
        //the users is a collection name .if the collection is already created then it will not create the same one.
        mFirestore.collection(Constants.USERS)
            //document id for the users fields here the document is the user id
            .document(userInfo.id)
            //if we want to merge later more user info like the image or the gender or mobile if we didn't do that at the registration phase.
            .set(userInfo, SetOptions.merge())

            .addOnSuccessListener {
                //here call a function from the base activity for transferring the result in it
                when(activity){
                  is  RegisterActivity ->{
                        activity.userRegistrationSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is  RegisterActivity ->{
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "error when registration the user", it)
                    }
                }
            }
    }//register user()
    fun registerUser(userInfo: User) {
        //the users is a collection name .if the collection is already created then it will not create the same one.
        mFirestore.collection(Constants.USERS)
            //document id for the users fields here the document is the user id
            .document(userInfo.id)
            //if we want to merge later more user info like the image or the gender or mobile if we didn't do that at the registration phase.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //here call a function from the base activity for transferring the result in it
                        Log.d("FireStoreClass", "success registration the user")
            }
            .addOnFailureListener {
                       Log.e("FireStoreClass", "error when registration the user", it)
            }
    }//register user()
    fun getCurrentUserID(): String {
        //an instance of current user using firebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        //a variable to assign the current user id if it is not null or else it will be black
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType:String) {
        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType+System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity,imageFileURI)
        )
        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener{taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener{uri ->
                        // START
                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is EditProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                                activity.hideProgressDialog()
                                activity.startActivity(Intent(activity,DashboardActivity::class.java))
                            }
                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is EditProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String,Any>){
        mFirestore
            .collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener{
                when(activity){
                    is EditProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is EditProfileActivity->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while updating the user details.",it)

            }
    }//updateUserProfileData()
}