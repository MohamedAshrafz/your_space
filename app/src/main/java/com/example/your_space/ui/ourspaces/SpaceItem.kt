package com.example.your_space.ui.ourspaces

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SpaceItem (
    var spaceName : String,
    var location : String,
    var mobile : String,
    var rating : String,
    var price : String,
    var description : String,
    val img : String ) : Parcelable