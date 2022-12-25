package com.example.your_space.ui.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.your_space.R
import com.example.your_space.databinding.FragmentFirstBinding
import com.example.your_space.databinding.ItemBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val homeViewModel by activityViewModels<HomeViewModel>()
        homeViewModel.list.observe(viewLifecycleOwner, Observer { list ->
            for (item in list){
                val homeItemBinding = ItemBinding.inflate(inflater, container, false)

                homeItemBinding.homeItem = item
                homeItemBinding.lifecycleOwner = this

                binding.linearLayoutList.addView(homeItemBinding.itemLayout)
            }
        })
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