package com.example.your_space.ui.ourspaces

import android.media.SoundPool
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.databinding.CoworkingSpaceItemBinding
import javax.xml.transform.Source

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

