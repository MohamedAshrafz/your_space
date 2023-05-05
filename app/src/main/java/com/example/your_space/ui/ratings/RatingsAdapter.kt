package com.example.your_space.ui.ratings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.database.RatingDB
import com.example.your_space.databinding.RatingItemBinding


class RatingsAdapter() : ListAdapter<RatingDB, RatingsAdapter.RatingItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingItemViewHolder {
        return RatingItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RatingItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class RatingItemViewHolder(private val binding: RatingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RatingDB) {
            binding.ratingItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RatingItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // use this way of inflating
                val binding = RatingItemBinding.inflate(layoutInflater, parent, false)
                // to not use
                //val binding = AsteroidItemViewBinding.inflate(layoutInflater)

                return RatingItemViewHolder(binding)
            }
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<RatingDB>() {
        override fun areItemsTheSame(oldItem: RatingDB, newItem: RatingDB): Boolean {
            return oldItem.ratingId == newItem.ratingId
        }

        override fun areContentsTheSame(oldItem: RatingDB, newItem: RatingDB): Boolean {
            return oldItem == newItem
        }

    }


}