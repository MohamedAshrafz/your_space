package com.example.your_space.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.your_space.R
import com.example.your_space.databinding.FragmentBookingBinding
import com.google.android.material.tabs.TabLayoutMediator

const val CURRENT_OR_HISTORY_KEY = "RecyclerViewIndex"

class BookingFragment : Fragment() {
    private lateinit var _binding: FragmentBookingBinding
    val binding
        get() = _binding

    val bookingAppViewModel by activityViewModels<BookingViewModel>()

    companion object {
        const val bookingTabPosition = 0
        const val historyTabPosition = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookingBinding.inflate(inflater, container, false)

        val viewPagerAdaptor = BookingViewPagerAdaptor(this)

        val viewPager = binding.bookingViewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = viewPagerAdaptor
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                bookingTabPosition -> tab.text = resources.getText(R.string.onGoing_text)
                historyTabPosition -> tab.text = resources.getText(R.string.History_text)
            }
        }.attach()


        return binding.root
    }

    class BookingViewPagerAdaptor(
        fragments: Fragment,
    ) : FragmentStateAdapter(fragments) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            // you can make any type of fragment for every position
            // does not need to be of the same type
            val fragment = CurrentBookingPageFragment()
            fragment.arguments = Bundle().apply {
                // We pass a String to identify which RecyclerView to use for each fragment
                // this is done using Bundled args
                when (position) {
                    bookingTabPosition -> putString(
                        CURRENT_OR_HISTORY_KEY,
                        RecyclerType.CURRENT.name
                    )
                    historyTabPosition -> putString(
                        CURRENT_OR_HISTORY_KEY,
                        RecyclerType.HISTORY.name
                    )
                }
            }
            return fragment
        }
    }
}