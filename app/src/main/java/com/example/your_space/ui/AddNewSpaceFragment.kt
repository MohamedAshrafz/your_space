package com.example.your_space.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentAddNewSpaceBinding
import com.example.your_space.ui.ourspaces.SpaceItem


class AddNewSpaceFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.let {
//            it.visibility = View.GONE
//        }
        val binding = FragmentAddNewSpaceBinding.inflate(inflater)
        binding.lifecycleOwner = this


//        val appViewModel by activityViewModels<AppViewModel>()

        binding.btnAddNewSpace.setOnClickListener {

            val spaceItem = SpaceItem(
                spaceName = binding.etSpaceName.text.toString(),
                location = binding.etSpaceLocation.text.toString(),
                mobile = binding.etSpacePhone.text.toString(),
                rating = "4.5",
                price = "20-30 per hour",
                description = binding.etSpaceDescription.text.toString(),
                img = "drawable-v24/coworking.jpg"
            )
//            if (!appViewModel.isEmptySpace(spaceItem)){
//                appViewModel.spacesList.value?.apply {
//                    add(spaceItem)
//                }
//                findNavController().navigate(AddNewSpaceFragmentDirections.actionAddNewSpaceFragmentToSecondFragment())
//            }
//            else Toast.makeText(context,"Plaese Fill All Data",Toast.LENGTH_SHORT).show()

        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}