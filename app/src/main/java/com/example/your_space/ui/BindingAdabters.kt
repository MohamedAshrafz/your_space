package com.example.your_space.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.database.BookingDB
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.database.WorkingSpaceDB
//import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.booking.BookingRecyclerViewAdaptor
import com.example.your_space.ui.ourspaces.OurSpacesAdabter
import com.example.your_space.ui.rooms.RoomsAdapter


@BindingAdapter("spaceItemsList")
fun bindSpacesList(recyclerView: RecyclerView, list: List<WorkingSpaceDB>?) {

    val adapter = recyclerView.adapter as OurSpacesAdabter
    adapter.submitList(list)
}

@BindingAdapter("bookingItemsList")
fun bindBookingsList(recyclerView: RecyclerView, listB: List<BookingDB>?) {

    val adapter = recyclerView.adapter as BookingRecyclerViewAdaptor
    adapter.submitList(listB)
}

@BindingAdapter("roomItemsList")
fun bindRoomsList(recyclerView: RecyclerView, list: List<SpaceRoomDB>?) {

    val adapter = recyclerView.adapter as RoomsAdapter
    adapter.submitList(list)
}