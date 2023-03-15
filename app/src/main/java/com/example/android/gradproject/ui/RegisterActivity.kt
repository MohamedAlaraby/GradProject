package com.example.android.gradproject.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.android.gradproject.utils.BaseActivity
import com.example.android.gradproject.utils.FireStoreClass
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.ActivityRegisterBinding
import com.example.android.gradproject.ui.loginscreen.LoginActivity
import com.example.domain.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {
    var mAuth: FirebaseAuth? = null
    var email: String = ""
    var password: String = ""
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        mAuth = FirebaseAuth.getInstance()

        binding.btnRegisterRegisterActivity.setOnClickListener {
            registerUser()
        }

    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.edFullName.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(
                binding.edEmailRegisterActivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(
                binding.edPasswordRegisterActivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(
                binding.edMobileRegisterActivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                return true
            }
        }
    }

    fun setupActionBar() {
        setSupportActionBar(binding.toolbarRegisterActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title=""
        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
        email = binding.edEmailRegisterActivity.text.toString()
        password = binding.edPasswordRegisterActivity.text.toString()
        showProgressDialog()
            mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {task->
                    if (task.isSuccessful) {
                        val firebaseUser:FirebaseUser?=task.result.user
                        val user= User(
                            id=firebaseUser!!.uid,
                            fullName = binding.edFullName.text.toString().trim { it<=' '},
                            email= binding.edEmailRegisterActivity.text.toString().trim { it<=' '},
                            mobile =binding.edMobileRegisterActivity.text.toString().trim { it<=' '}.toLong()
                        )
                        FireStoreClass().registerUser(this@RegisterActivity,user)
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show()
                        sendVerification()
                    } else {
                        hideProgressDialog()
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun sendVerification() {
        val user = mAuth?.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast.makeText(this,"sendVerification error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun userRegistrationSuccess() {
        //Hide the progress bar
        hideProgressDialog()
        Toast.makeText(
            this, resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}