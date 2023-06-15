package com.example.your_space.ui.ourspaces

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.your_space.R
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.databinding.CoworkingSpaceItemBinding
import com.example.your_space.network.Constants.BASE_URL
import com.example.your_space.network.DEFAULT_IMAGE_INDEX
import com.example.your_space.network.IMAGE_VS_SPACEID_ENDPOINT

class OurSpacesAdabter(private val clickListener: (spaceItem: WorkingSpaceDB) -> Unit) :
    ListAdapter<WorkingSpaceDB, OurSpacesAdabter.SpaceItemViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpaceItemViewHolder {
        return SpaceItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SpaceItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class SpaceItemViewHolder private constructor(private val binding: CoworkingSpaceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkingSpaceDB, clickListener: (spaceItem: WorkingSpaceDB) -> Unit) {
//            binding.clickListener = clickListener
            binding.spaceItem = item
            binding.constraintLayoutSpaceItem.setOnClickListener { clickListener(item) }
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): SpaceItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // use this way of inflating
                val binding = CoworkingSpaceItemBinding.inflate(layoutInflater, parent, false)
                // to not use
                //val binding = AsteroidItemViewBinding.inflate(layoutInflater)

                return SpaceItemViewHolder(binding)
            }
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<WorkingSpaceDB>() {
        override fun areItemsTheSame(oldItem: WorkingSpaceDB, newItem: WorkingSpaceDB): Boolean {
            return oldItem.spaceId == newItem.spaceId
        }

        override fun areContentsTheSame(oldItem: WorkingSpaceDB, newItem: WorkingSpaceDB): Boolean {
            return oldItem == newItem
        }

    }


}

// take care of the nullable String
// (after opening the app the imageUrl will be null till getting the value,
// after fetching the data from the remote server)
@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
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

// take care of the nullable String
// (after opening the app the imageUrl will be null till getting the value,
// after fetching the data from the remote server)
// you cannot set the default value for any parameter instead check for the nullability manually
// check for more in https://developer.android.com/topic/libraries/data-binding/binding-adapters
@BindingAdapter(value = ["spaceIdForImage", "index"], requireAll = false)
fun bindImage(imageView: ImageView, spaceId: String?, index: String?) {

    val imageUrl = if (index == null){
        "$BASE_URL$IMAGE_VS_SPACEID_ENDPOINT$spaceId/$DEFAULT_IMAGE_INDEX"
    }else{
        "$BASE_URL$IMAGE_VS_SPACEID_ENDPOINT$spaceId/$index"
    }

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
