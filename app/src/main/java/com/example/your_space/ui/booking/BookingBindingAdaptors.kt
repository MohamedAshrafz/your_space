package com.example.your_space.ui.booking

import android.app.AlertDialog
import android.graphics.drawable.Icon
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.R
import com.example.your_space.database.BookingDB
import com.example.your_space.databinding.BookItemBinding

enum class ButtonType(val asString: String) {
    CANCEL("Cancel"),
    DELETE("Delete")
}

class BookingRecyclerViewAdaptor(
    private val clickListener: (bookItem: BookingDB) -> Unit,
    private val type: String
) :
    ListAdapter<BookingDB, BookingRecyclerViewAdaptor.BookItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder.from(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, type)
    }

    class BookItemViewHolder(private val binding: BookItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(item: BookingDB, clickListener: (bookItem: BookingDB) -> Unit, type: String) {
//            binding.funcButton.text = type

            if (type == ButtonType.CANCEL.asString) {
                binding.funcButton.setImageIcon(
                    Icon.createWithResource(
                        binding.root.context,
                        R.drawable.ic_baseline_cancel_24
                    )
                )
            } else {
                binding.funcButton.setImageIcon(
                    Icon.createWithResource(
                        binding.root.context,
                        R.drawable.ic_baseline_delete_24
                    )
                )
            }
            binding.bookItem = item
            binding.funcButton.setOnClickListener {
                AlertDialog.Builder(binding.root.context).apply {
                    setTitle(R.string.warning_string)
                    setMessage("Do you want to $type this booking?")
                    setPositiveButton(R.string.yes_string) { _, _ ->
                        clickListener(item)
                    }
                    setNegativeButton(R.string.no_string) { dialog, _ -> dialog.cancel() }
                    show()
                }
            }

//            binding.constraintLayoutBookItem.setOnCreateContextMenuListener { menu, _, _ ->
//
//                menu.add(type).setTitle(type).setOnMenuItemClickListener {
//                    AlertDialog.Builder(binding.root.context).apply {
//                        setTitle(R.string.warning_string)
//                        setMessage("Do you want to $type this booking?")
//                        setPositiveButton(R.string.yes_string) { _, _ ->
//                            clickListener(item)
//                        }
//                        setNegativeButton(R.string.no_string) { dialog, _ -> dialog.cancel() }
//                        show()
//                    }
//                    return@setOnMenuItemClickListener true
//                }
//            }

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