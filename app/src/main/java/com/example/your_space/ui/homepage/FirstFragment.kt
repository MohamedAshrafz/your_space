package com.example.your_space.ui.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.your_space.R
import com.example.your_space.databinding.FragmentFirstBinding
import com.example.your_space.databinding.ItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

enum class FragmentEnumForIndexing(val index: Int) {
    OUR_SPACES(index = 1),
    YOUR_BOOKINGS(index = 2),
    MAPS(index = 3),
}

class ClassListener(
    private val fragmentIndex: Int,
    private val activity: FragmentActivity,
    private val navController: NavController
) {
    fun getListener() {
        val menuItem =
            activity.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.menu?.get(
                fragmentIndex
            )
        if (menuItem != null) {
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }
    }
}

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!


    private val homeAppViewModel by activityViewModels<HomeViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        homeAppViewModel.homeList.observe(viewLifecycleOwner) { list ->
            for (item in list) {

                val listCL = mutableListOf<ClassListener>()

                listCL.apply {
                    add(
                        ClassListener(
                            FragmentEnumForIndexing.OUR_SPACES.index,
                            requireActivity(),
                            findNavController()
                        )
                    )
                    add(
                        ClassListener(
                            FragmentEnumForIndexing.YOUR_BOOKINGS.index,
                            requireActivity(),
                            findNavController()
                        )
                    )
                    add(
                        ClassListener(
                            FragmentEnumForIndexing.MAPS.index,
                            requireActivity(),
                            findNavController()
                        )
                    )
                }

                val homeItemBinding = ItemBinding.inflate(inflater, container, false)

                homeItemBinding.homeItem = item
                homeItemBinding.itemImg.setImageDrawable(requireContext().getDrawable(item.img))

                // has to but a function not a reference to a function
                homeItemBinding.itemLayout.setOnClickListener { listCL[list.indexOf(item)].getListener() }

                homeItemBinding.lifecycleOwner = viewLifecycleOwner

                binding.linearLayoutList.addView(homeItemBinding.itemLayout)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}