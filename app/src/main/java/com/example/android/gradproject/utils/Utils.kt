package com.example.android.gradproject.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.android.gradproject.R
import java.io.IOException

class Utils(val context:Context):BaseActivity() {
    fun loadUserPicture(image:Uri,imageView: ImageView){
        try {
            //Load the user mage in the image view
            Glide
                .with(context)
                .load(image)
                .centerCrop()//scale type of the image
                .placeholder(R.drawable.ic_user_placeholder)//a default place holder the loading fail
                .into(imageView)
        }catch(ex: IOException){
            ex.printStackTrace()
        }
    }
    fun loadUserPicture(image:String,imageView: ImageView){
        try {
            //Load the user mage in the image view
            Glide
                .with(context)
                .load(Uri.parse(image).toString())
                .centerCrop()//scale type of the image
                .placeholder(R.drawable.ic_user_placeholder)//a default place holder the loading fail
                .into(imageView)
        }catch(ex: IOException){
            ex.printStackTrace()
        }
    }



}