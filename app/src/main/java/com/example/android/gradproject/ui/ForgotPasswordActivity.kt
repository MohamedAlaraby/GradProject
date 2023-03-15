package com.example.android.gradproject.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.ActivityForgotPasswordBinding
import com.example.android.gradproject.utils.BaseActivity
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupActionBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding.btnSubmit.setOnClickListener {
            val email=binding.etLoginEmail.text.toString().trim { it<= ' ' }
            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            }else{
                showProgressDialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).
                addOnCompleteListener {task->
                    hideProgressDialog()
                    if (task.isSuccessful)
                    {
                        Toast.makeText(this,"email sent successfully,check your gmail", Toast.LENGTH_LONG).show()
                        finish()
                    }else
                    {
                        Toast.makeText(this,task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    }
    fun setupActionBar(){
        setSupportActionBar(binding.toolbarForgotpasswordActivity)
        val actionBar =supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title=""
        }
        binding.toolbarForgotpasswordActivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}