package com.example.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val  id:String="",
    val  fullName:String="",
    val  email:String="",
    val  image:String="",
    val  mobile:Long=0,
): Parcelable