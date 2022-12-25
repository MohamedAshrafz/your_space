package com.example.your_space.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.ui.ourspaces.OurSpacesAdabter
import com.example.your_space.ui.ourspaces.SpaceItem

@BindingAdapter("spaceItemsList")
fun bindAsteroidsList(recyclerView: RecyclerView, list: List<SpaceItem>?) {

    val adapter = recyclerView.adapter as OurSpacesAdabter
    adapter.submitList(list)
}