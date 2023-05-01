package com.example.your_space.ui.rooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.databinding.RoomitemBinding


class RoomsAdapter(
    private val clickListener: (roomItem: SpaceRoomDB) -> Unit
) : ListAdapter<SpaceRoomDB, RoomsAdapter.RoomItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RoomItemViewHolder {
        return RoomItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class RoomItemViewHolder(private val binding: RoomitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SpaceRoomDB, clickListener: (roomItem: SpaceRoomDB) -> Unit) {
            binding.roomBookBtn.setOnClickListener { clickListener(item) }
            binding.roomItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RoomItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // use this way of inflating
                val binding = RoomitemBinding.inflate(layoutInflater, parent, false)
                // to not use
                //val binding = AsteroidItemViewBinding.inflate(layoutInflater)

                return RoomItemViewHolder(binding)
            }
        }

    }

    object DiffCallback : DiffUtil.ItemCallback<SpaceRoomDB>() {
        override fun areItemsTheSame(oldItem: SpaceRoomDB, newItem: SpaceRoomDB): Boolean {
            return oldItem.roomId == newItem.roomId
        }

        override fun areContentsTheSame(oldItem: SpaceRoomDB, newItem: SpaceRoomDB): Boolean {
            return oldItem == newItem
        }

    }


}