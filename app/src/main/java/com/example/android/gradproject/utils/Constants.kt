package com.example.android.gradproject.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {


    //our collections in the cloud firestore
    const val USERS="users"
    const val EXTRA_USER_DETAILS="extra_user_details"
    const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE=1
    const val PICK_IMAGE_REQUEST_CODE=0

    const val FIRST_NAME="firstName"
    const val LAST_NAME="lastName"
    const val MOBILE="mobile"
    const val IMAGE="image"

    const val USER_ID="user_id"
    const val PROFILE_COMPLETED: String="profileCompleted"
    const val USER_PROFILE_IMAGE:String="user_profile_image"
    const val BUTTON_TEXT_SIZE = 14

    fun showImageChooser(activity: Activity){
        //an implicit intent to launch image selection of the phone storage
       val galleryIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
       activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }







}