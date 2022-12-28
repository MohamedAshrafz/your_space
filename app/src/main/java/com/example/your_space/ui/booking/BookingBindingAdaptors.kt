package com.example.your_space.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.databinding.BookItemBinding
import com.example.your_space.databinding.CoworkingSpaceItemBinding
import com.example.your_space.ui.ourspaces.OurSpacesAdabter
import com.example.your_space.ui.ourspaces.SpaceItem

class BookingRecyclerViewAdaptor(
    private val clickListener: (bookItem: BookItem) -> Unit,
    private val type: String
) :
    ListAdapter<BookItem, BookingRecyclerViewAdaptor.BookItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, type)
    }

    class BookItemViewHolder(private val binding: BookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookItem, clickListener: (bookItem: BookItem) -> Unit, type: String) {
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

}

object DiffCallback : DiffUtil.ItemCallback<BookItem>() {

    override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("bookList")
fun bindAsteroidsList(recyclerView: RecyclerView, list: List<BookItem>?) {
    val adapter = recyclerView.adapter as BookingRecyclerViewAdaptor
    adapter.submitList(list)
}