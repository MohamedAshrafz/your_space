package com.example.your_space.ui.booking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class BookItem(
    val bookName: String,
    val date: String,
    val time: String
) : Parcelable