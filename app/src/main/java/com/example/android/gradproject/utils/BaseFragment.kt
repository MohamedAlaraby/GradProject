package com.example.android.gradproject.utils


import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.android.gradproject.R
import javax.sql.DataSource

open class BaseFragment : Fragment() {
    private lateinit var mProgressDialog: AlertDialog
    fun showProgressDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_progress, null)
        builder.setView(dialogView)
        //to make the user unable to delete it
        builder.setCancelable(false)
        mProgressDialog = builder.create()
        //show
        mProgressDialog.show()

    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}