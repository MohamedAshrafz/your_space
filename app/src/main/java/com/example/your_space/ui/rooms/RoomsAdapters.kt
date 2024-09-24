package com.example.your_space.ui.rooms

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.your_space.R
import com.example.your_space.network.Constants
import com.example.your_space.network.DEFAULT_IMAGE_INDEX
import com.example.your_space.network.IMAGE_VS_ROOMID_ENDPOINT
import com.example.your_space.repository.AppRepository

// take care of the nullable String
// (after opening the app the imageUrl will be null till getting the value,
// after fetching the data from the remote server)
// you cannot set the default value for any parameter instead check for the nullability manually
// check for more in https://developer.android.com/topic/libraries/data-binding/binding-adapters
@BindingAdapter(value = ["roomIdForImage", "index"], requireAll = false)
fun bindImage(imageView: ImageView, roomId: String?, index: String?) {

    val imageUrl = if (index == null) {
        "${Constants.getBaseUrl()}$IMAGE_VS_ROOMID_ENDPOINT$roomId/$DEFAULT_IMAGE_INDEX"
    } else {
        "${Constants.getBaseUrl()}$IMAGE_VS_ROOMID_ENDPOINT$roomId/$index"
    }

    val repository = AppRepository.getInstance(imageView.context)

    if (repository.getSession() != null){
        val glideUrl = GlideUrl(
            imageUrl,
            LazyHeaders.Builder().addHeader("Cookie", repository.getSession()).build()
        )

        Glide.with(imageView.context)
            .load(glideUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    } else {
        val imageUri = imageUrl.toUri().buildUpon().scheme("http").build()

        Glide.with(imageView.context)
            .load(imageUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)
    }
}