package com.example.your_space.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.your_space.R
import com.example.your_space.databinding.FragmentBookingBinding
import com.example.your_space.ui.AppViewModel
import com.example.your_space.ui.RecyclerType
import com.google.android.material.tabs.TabLayoutMediator

const val CURRENT_OR_HISTORY_KEY = "RecyclerViewIndex"
class BookingFragment : Fragment() {
    private lateinit var _binding: FragmentBookingBinding
    val binding
        get() = _binding

    private var inHistoryTab = false

    companion object {
        const val bookingTabPosition = 0
        const val historyTabPosition = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookingBinding.inflate(inflater, container, false)

        val bookingAppViewModel by activityViewModels<AppViewModel>()

        val viewPagerAdaptor = BookingViewPagerAdaptor(this)

        val viewPager = binding.bookingViewPager
        val tabLayout = binding.tabLayout

        viewPager.adapter = viewPagerAdaptor
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL



        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                bookingTabPosition -> tab.text = resources.getText(R.string.onGoing_text)
                historyTabPosition -> tab.text = resources.getText(R.string.History_text)
            }
        }.attach()


        return binding.root
    }

    class BookingViewPagerAdaptor(
        private val fragments: Fragment,
    ) : FragmentStateAdapter(fragments) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = CurrentBookingPageFragment()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                when (position) {
                    0 -> putString(CURRENT_OR_HISTORY_KEY, RecyclerType.CURRENT.name)
                    else -> putString(CURRENT_OR_HISTORY_KEY, RecyclerType.HISTORY.name)
                }
            }
            return fragment
        }
    }
}