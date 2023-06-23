package com.example.your_space.ui.Ratingss

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.database.RatingsDB
import com.example.your_space.databinding.RatingItemBinding


class RatingsAdapter : ListAdapter<RatingsDB, RatingsAdapter.RatingsItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RatingsItemViewHolder {
        return RatingsItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RatingsItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class RatingsItemViewHolder(private val binding: RatingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RatingsDB) {
            binding.ratingsItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RatingsItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // use this way of inflating
                val binding = RatingItemBinding.inflate(layoutInflater, parent, false)
                // to not use
                //val binding = AsteroidItemViewBinding.inflate(layoutInflater)

                return RatingsItemViewHolder(binding)
            }
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<RatingsDB>() {
        override fun areItemsTheSame(oldItem: RatingsDB, newItem: RatingsDB): Boolean {
            return oldItem.ratingId == newItem.ratingId
        }

        override fun areContentsTheSame(oldItem: RatingsDB, newItem: RatingsDB): Boolean {
            return oldItem == newItem
        }

    }
}

@BindingAdapter("ratingsItemsList")
fun bindRatingsList(recyclerView: RecyclerView, list: List<RatingsDB>?) {

    val adapter = recyclerView.adapter as RatingsAdapter
    adapter.submitList(list)
}