package com.example.android.gradproject.ui.dashboardscreen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.android.gradproject.ConnectionLiveData
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.ActivityDashboardBinding
import com.example.android.gradproject.utils.BaseActivity
import com.example.android.gradproject.utils.Utils
import com.example.domain.entity.User
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : BaseActivity() {

    lateinit var connectionLiveData: ConnectionLiveData
    private var userDetails: User?=null
    private lateinit var navView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var navController: NavController
    private var headerView: View? = null
    private val viewModel:DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getUserDetails()
        setSupportActionBar(binding.appBarDashboard.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        headerView = navView.getHeaderView(0)
        connectionLiveData= ConnectionLiveData(this)
        val  tvConnectionState=findViewById<TextView>(R.id.tv_connection_state)

        connectionLiveData.observe(this, Observer {isNetworkAvailable->
            if (isNetworkAvailable==false){
                tvConnectionState.visibility=View.VISIBLE
                tvConnectionState.text="There is no internet connection"
            }else{
                tvConnectionState.visibility=View.GONE
            }
        })


        navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Show what the things that will will appear on the action bar
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
        ), drawerLayout)


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        listener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                //what do you want after press on certain item in the drawer.
                when (destination.id) {
                    R.id.nav_home -> {

                    }
                    R.id.nav_gallery -> {

                    }
                    R.id.nav_slideshow -> {
                    }
                }
            }
        //For the epic bottom navigation bar
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setupWithNavController(navController)
        viewModel.getUserDetails()
        lifecycleScope.launch{
            viewModel.userDetails.collect{
                if (it != null){
                    userDetails=it
                    updateHeaderView(it)
                    setUserImage(it.image)
                }
            }
        }
    }//end of on create

    private fun updateHeaderView(userDetails:User?) {
        if (userDetails !=null){
            var userImage: ImageView = headerView!!.findViewById(R.id.headerView_iv)
            var tv_name :TextView=     headerView!!.findViewById(R.id.headerView_tvName)
            var tv_email:TextView =    headerView!!.findViewById(R.id.headerView_tvEmail)
            Utils(this).loadUserPicture(userDetails.image,userImage)
            tv_name.text=userDetails.fullName
            tv_email.text=userDetails.email
        }
    }
    private fun setUserImage(userImageUrl: String) {
        Glide.with(applicationContext)
            .asBitmap()
            .load(userImageUrl)
            .optionalCircleCrop()
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    binding.bottomNavigationView.itemIconSize = 100
                    binding.bottomNavigationView.itemIconTintList = null;
                    val profileImage: Drawable = BitmapDrawable(resources, resource)
                    binding.bottomNavigationView
                        .menu
                        .getItem(4).icon = profileImage
                }
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            }
            )
    }
    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(listener)
    }
    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(listener)
    }
    override fun onSupportNavigateUp(): Boolean {
        //  val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}