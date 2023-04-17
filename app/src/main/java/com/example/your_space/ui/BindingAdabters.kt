package com.example.your_space.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.booking.BookingRecyclerViewAdaptor
import com.example.your_space.ui.ourspaces.OurSpacesAdabter
import com.example.your_space.ui.ourspaces.SpaceItem

@BindingAdapter("spaceItemsList")
fun bindSpacesList(recyclerView: RecyclerView, list: List<SpaceItem>?) {

    val adapter = recyclerView.adapter as OurSpacesAdabter
    adapter.submitList(list)
}

@BindingAdapter("bookingItemsList")
fun bindBookingsList(recyclerView: RecyclerView, listB: List<BookItem>?) {

    val adapter = recyclerView.adapter as BookingRecyclerViewAdaptor
    adapter.submitList(listB)
}