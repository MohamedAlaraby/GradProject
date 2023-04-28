package com.example.android.gradproject.ui
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android.gradproject.R
import com.example.android.gradproject.databinding.FragmentDisplyProfileBinding
import com.example.android.gradproject.ui.dashboardscreen.DashboardViewModel
import com.example.android.gradproject.utils.BaseFragment
import com.example.android.gradproject.utils.Constants

import com.example.android.gradproject.utils.Utils
import com.example.domain.entity.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class DisplayProfileFragment : BaseFragment(), View.OnClickListener {
    private var mUserDetails: User? = null
    private val viewModel: DashboardViewModel by viewModels()
    lateinit var binding: FragmentDisplyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDisplyProfileBinding.inflate(inflater, container, false)
        binding.tvEdit.setOnClickListener(this)
        viewModel.getUserDetails()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.userDetails.collect {
                if (it != null){
                    mUserDetails = it
                    displayDetails(mUserDetails)
                }
            }
        }
    }
    private fun displayDetails(mUserDetails: User?){
        if (mUserDetails !=null){
            binding.tvName.text = mUserDetails.fullName
            binding.tvEmail.text = mUserDetails.email
            binding.tvMobileNumber.text = mUserDetails.mobile.toString()
            if (mUserDetails.image != null) {
                Utils(requireContext()).loadUserPicture(
                    mUserDetails.image,
                    binding.ivUserPhoto
                )
            }
        }
}
override fun onClick(v: View?) {
    if (v != null) {
        when (v.id) {
            R.id.tv_edit -> {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                if (mUserDetails != null)
                {
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                }
                startActivity(intent)
            }
        }
    }
}
}