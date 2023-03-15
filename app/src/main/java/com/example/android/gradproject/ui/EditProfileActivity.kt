package com.example.android.gradproject.ui
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.ActivityEditProfileBinding
import com.example.android.gradproject.ui.dashboardscreen.DashboardActivity
import com.example.android.gradproject.utils.BaseActivity
import com.example.android.gradproject.utils.Constants
import com.example.android.gradproject.utils.FireStoreClass
import com.example.android.gradproject.utils.Utils
import com.example.domain.entity.User
import java.io.IOException

@Suppress("DEPRECATION")
class EditProfileActivity : BaseActivity(),View.OnClickListener {
    private lateinit var mUserDetails: User
    //the uri from your own mobile gallery
    private  var mSelectedImageFileUri: Uri?=null
    //the url from the web browser
    private  var mUserProfileImageURL:String?=""

    lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
            // Here, the some of the edittext components are disabled because it is added at a time of Registration.
            binding.etFullName.setText(mUserDetails.fullName)
            binding.etEmail.setText(mUserDetails.email)
            // Load the image using the GlideLoader class with the use of Glide Library.
            Utils(this).loadUserPicture(mUserDetails.image, binding.ivUserProfilePhoto)
            // Set the existing values to the UI and allow user to edit except the Email ID.
            if (mUserDetails.mobile != 0L){
                binding.etMobileNumberProfileActivity.setText(mUserDetails.mobile.toString())
            }
        }
        // Assign the on click event to the user profile photo.
        binding.ivUserProfilePhoto.setOnClickListener(this@EditProfileActivity)
        // Assign the on click event to the SAVE button.
        binding.btnSaveUserProfileDetails.setOnClickListener(this@EditProfileActivity)
    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_profile_photo -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                        )
                    }
                }
                R.id.btn_save_user_profile_details -> {
                    if (validateUserProfileDetails()) {
                        // Show the progress dialog.
                       // showProgressDialog()
                        if (mSelectedImageFileUri != null) {
                            showProgressDialog()
                            FireStoreClass().uploadImageToCloudStorage(
                                this@EditProfileActivity,
                                mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE
                            )
                        } else {
                            showProgressDialog()
                            updateUserProfileDetails()

                        }

                    }
                }
            }
        }
    }//on click()
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarUserProfileActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title= resources.getString(R.string.title_edit_profile)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue)
        }
        binding.toolbarUserProfileActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etMobileNumberProfileActivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        // Get the FirstName from editText and trim the space
        val fullName = binding.etFullName.text.toString().trim { it <= ' ' }
        if (fullName != mUserDetails.fullName) {
            userHashMap[Constants.FIRST_NAME] = fullName
        }
        // Here we get the text from editText and trim the space
        val mobileNumber = binding.etMobileNumberProfileActivity.text.toString().trim { it <= ' ' }

        if (mUserProfileImageURL!!.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL!!
        }
        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        // call the registerUser function of FireStore class to make an entry in the database.
        FireStoreClass().updateUserProfileData(
            this@EditProfileActivity,
            userHashMap
        )
    }//end of updateUserProfileDetails()
    fun userProfileUpdateSuccess() {
        // Hide the progress dialog
        hideProgressDialog()
        Toast.makeText(
            this@EditProfileActivity,
            resources.getString(R.string.profile_updated_successfully),
            Toast.LENGTH_SHORT
        ).show()
        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@EditProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied_string),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }//onRequestPermissionsResult

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode ==RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        Utils(this).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.ivUserProfilePhoto
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@EditProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode ==RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }
}