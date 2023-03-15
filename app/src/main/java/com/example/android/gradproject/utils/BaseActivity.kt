package com.example.android.gradproject.utils


import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.DialogProgressBinding
import com.google.android.material.snackbar.Snackbar


@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {
    private lateinit var mProgressDialog:AlertDialog

    fun showErrorSnackBar(message:String,errorMessage:Boolean){
        val snackbar=Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView=snackbar.view
        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError)
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarSuccess)
            )
        }
        snackbar.show()
    }


    fun showProgressDialog(){
        val builder= AlertDialog.Builder(this)
        val dialog_view=layoutInflater.inflate(R.layout.dialog_progress,null)
        builder.setView(dialog_view)
        //to make the user unable to delete it
        builder.setCancelable(false)
        mProgressDialog=builder.create()
        //show
        mProgressDialog.show()
    }
    fun hideProgressDialog(){
        if (mProgressDialog!=null){
            mProgressDialog.dismiss()
        }

    }
}//the class