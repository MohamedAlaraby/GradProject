@file:Suppress("DEPRECATION")

package com.example.android.gradproject.ui.loginscreen
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.android.gradproject.utils.BaseActivity
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.ActivityLoginBinding
import com.example.android.gradproject.ui.ForgotPasswordActivity
import com.example.android.gradproject.ui.RegisterActivity
import com.example.android.gradproject.ui.dashboardscreen.DashboardActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
class LoginActivity : BaseActivity() {
    companion object {
        private const val RC_SIGN_IN = 9001
    }
    private lateinit var viewModel: SignInViewModel
    lateinit var binding: ActivityLoginBinding
    private var mAuth: FirebaseAuth? = null
    private var email_login = "empty"
    private var password_login = "empty"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        val application = requireNotNull(this).application
        val factory = SignInViewModelFactory(application, object: OnSignInStartedListener {
            override fun onSignInStarted(client: GoogleSignInClient?) {
                client?.signInIntent?.let { startActivityForResult(it, RC_SIGN_IN) }
            }
        })
        viewModel = ViewModelProvider(this, factory).get(SignInViewModel::class.java)

        binding.ivRegisterWithGoogle.setOnClickListener {
            showProgressDialog()
            viewModel.signIn()
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            showProgressDialog()
            loginWithEmailAndPassword()

        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }//oncreate
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK && data != null) {
            // this task is responsible for getting ACCOUNT SELECTED
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.firebaseAuthWithGoogle(account.idToken!!)
                Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT)
                    .show()
                hideProgressDialog()
                navigateToDashboardActivity()

            } catch (e: ApiException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                hideProgressDialog()
            }
        }else{
            hideProgressDialog()
        }
    }
    private fun loginWithEmailAndPassword() {
        email_login = binding.etEmailLoginActivity.text.toString()
        password_login = binding.etPasswordLoginActivity.text.toString()

        if (validateLoginDetails()) {
            mAuth?.signInWithEmailAndPassword(email_login, password_login)?.
                   addOnCompleteListener {
                hideProgressDialog()
                if (it.isSuccessful) {
                    //   verifyEmailAddress()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            hideProgressDialog()
            Toast.makeText(this, "Sorry the email or pass is empty",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmailLoginActivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(
                binding.etPasswordLoginActivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                return true
            }

        }
    }
    private fun navigateToDashboardActivity() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}