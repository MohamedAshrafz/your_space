package com.example.your_space.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.database.BookingDB
import com.example.your_space.databinding.BookItemBinding

class BookingRecyclerViewAdaptor(
    private val clickListener: (bookItem: BookingDB) -> Unit,
    private val type: String
) :
    ListAdapter<BookingDB, BookingRecyclerViewAdaptor.BookItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, type)
    }

    class BookItemViewHolder(private val binding: BookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookingDB, clickListener: (bookItem: BookingDB) -> Unit, type: String) {
            binding.funcButton.setOnClickListener { clickListener(item) }
            binding.funcButton.text = type
            binding.bookItem = item

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BookItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // use this way of inflating
                val binding = BookItemBinding.inflate(layoutInflater, parent, false)
                // to not use
                //val binding = AsteroidItemViewBinding.inflate(layoutInflater)

                return BookItemViewHolder(binding)
            }
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<BookingDB>() {

        override fun areItemsTheSame(oldItem: BookingDB, newItem: BookingDB): Boolean {
            return oldItem.bookingId == newItem.bookingId
        }

        override fun areContentsTheSame(oldItem: BookingDB, newItem: BookingDB): Boolean {
            return oldItem == newItem
        }
    }
}

@BindingAdapter("bookList")
fun bindAsteroidsList(recyclerView: RecyclerView, list: List<BookingDB>?) {
    val adapter = recyclerView.adapter as BookingRecyclerViewAdaptor
    adapter.submitList(list)
}