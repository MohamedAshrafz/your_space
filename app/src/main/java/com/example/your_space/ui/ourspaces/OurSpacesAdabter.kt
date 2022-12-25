package com.example.your_space.ui.ourspaces

import android.media.SoundPool
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.databinding.CoworkingSpaceItemBinding
import javax.xml.transform.Source

class OurSpacesAdabter(private val clickListener: (spaceItem: SpaceItem) -> Unit):
    ListAdapter<SpaceItem,OurSpacesAdabter.SpaceItemViewHolder>(DiffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpaceItemViewHolder {
        return SpaceItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SpaceItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class SpaceItemViewHolder private constructor(private val binding: CoworkingSpaceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SpaceItem, clickListener: (spaceItem: SpaceItem) -> Unit) {
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

    object DiffCallback : DiffUtil.ItemCallback<SpaceItem>(){
        override fun areItemsTheSame(oldItem: SpaceItem, newItem: SpaceItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SpaceItem, newItem: SpaceItem): Boolean {
            return oldItem.spaceName == newItem.spaceName
        }

    }


    }

