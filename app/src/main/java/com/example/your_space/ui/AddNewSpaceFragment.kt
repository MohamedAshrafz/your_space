package com.example.your_space.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.R
import com.example.your_space.databinding.FragmentAddNewSpaceBinding
import com.example.your_space.ui.ourspaces.SpaceItem


class AddNewSpaceFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.let {
//            it.visibility = View.GONE
//        }
        val binding = FragmentAddNewSpaceBinding.inflate(inflater)
        binding.lifecycleOwner = this



        val viewModel by activityViewModels<ViewModel>()

        binding.btnAddNewSpace.setOnClickListener {

            val spaceItem = SpaceItem(binding.etSpaceName.text.toString(),
                binding.etSpaceLocation.text.toString(),
                binding.etSpacePhone.text.toString(),
                "4.5",
                "20-30 per hour",
                binding.etSpaceDescription.text.toString(),
                "drawable-v24/coworking.jpg")
            if (!viewModel.isNull(spaceItem)){
                viewModel.spacesList.value?.apply {
                    add(spaceItem)
                }
                findNavController().navigate(AddNewSpaceFragmentDirections.actionAddNewSpaceFragmentToSecondFragment())
            }
            else Toast.makeText(context,"Plaese Fill All Data",Toast.LENGTH_SHORT).show()



        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}