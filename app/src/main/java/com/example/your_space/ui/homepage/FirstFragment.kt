package com.example.your_space.ui.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.your_space.R
import com.example.your_space.databinding.FragmentFirstBinding
import com.example.your_space.databinding.ItemBinding
import com.example.your_space.ui.AppViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class ClassListener(val clickLL: () -> Unit) {
}

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val listCL = mutableListOf<ClassListener>()

        listCL.apply {
            add(
                ClassListener {
                    val bottomNavigationView =
                        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                    val menuItem1 = bottomNavigationView?.menu?.get(1)
                    if (menuItem1 != null) {
                        NavigationUI.onNavDestinationSelected(menuItem1, findNavController())
                    }
                })
            add(
                ClassListener {
                    val bottomNavigationView =
                        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
                    val menuItem2 = bottomNavigationView?.menu?.get(2)
                    if (menuItem2 != null) {
                        NavigationUI.onNavDestinationSelected(menuItem2, findNavController())
                    }
                }
            )
            add(
                ClassListener {}
            )
        }

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val homeAppViewModel by activityViewModels<AppViewModel>()

        homeAppViewModel.homeList.observe(viewLifecycleOwner) { list ->
            for (item in list) {
                val homeItemBinding = ItemBinding.inflate(inflater, container, false)

                homeItemBinding.homeItem = item
                homeItemBinding.itemLayout.setOnClickListener { listCL[list.indexOf(item)].clickLL() }

                homeItemBinding.lifecycleOwner = this

                binding.linearLayoutList.addView(homeItemBinding.itemLayout)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}