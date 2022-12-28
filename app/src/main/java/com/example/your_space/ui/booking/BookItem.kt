package com.example.your_space.ui.booking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BookItem(
    val spaceName: String,
    val Date: String,
    val time: String
): Parcelable