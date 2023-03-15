@file:Suppress("DEPRECATION")

package com.example.android.gradproject.ui
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.android.gradproject.R
import com.example.android.gradproject.ui.dashboardscreen.DashboardActivity
import com.example.android.gradproject.ui.loginscreen.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val backgroundImg=findViewById<ImageView>(R.id.splash_screen_iv)
        val sideAnimation=AnimationUtils.loadAnimation(this, R.anim.slide)
        backgroundImg.startAnimation(sideAnimation)
        mAuth=FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        /*
        * if the user is not authenticated send him to the sign in activity
         */

        Handler().postDelayed({
            if (user!=null){
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        },2000)
    }
}