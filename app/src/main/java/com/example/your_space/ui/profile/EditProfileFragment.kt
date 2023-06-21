package com.example.your_space.ui.profile

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentEditProfileBinding
import com.example.your_space.ui.authentication.AuthenticationActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*


class EditProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var _binding: FragmentEditProfileBinding
    val binding
        get() = _binding

    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences(
            AuthenticationActivity.LOGIN_STATE,
            AppCompatActivity.MODE_PRIVATE
        )

        val userId = sp.getString(AuthenticationActivity.USER_ID, null)

        if (userId != null) {
            editProfileViewModel = ViewModelProvider(
                this,
                EditProfileViewModelFactory(requireActivity().application, userId)
            )[EditProfileViewModel::class.java]
        }

        binding.viewModel = editProfileViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        editProfileViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                editProfileViewModel.apply {
                    _firstName.value = user.firstName
                    _lastName.value = user.lastName
                    _mobileNo.value = user.mobileNo
                    _address.value = user.address
                    _birthDate.value = user.birthDate
                    _bio.value = user.bio
                }
            }
        }

        binding.saveButton.setOnClickListener {
            editProfileViewModel.updateUserAndInformBackendAndDatabase()
        }

        editProfileViewModel.snackbarText.observe(viewLifecycleOwner){ snackbarText ->
            if (!snackbarText.isNullOrEmpty()){
                Snackbar.make(requireView(), snackbarText, Snackbar.LENGTH_SHORT).show()
            }
        }

        editProfileViewModel.navigateBack.observe(viewLifecycleOwner) { isNavigate ->
            if (isNavigate) {
                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                editProfileViewModel.clearNavigateBack()
            }
        }

        binding.birthDateIte.setOnClickListener {
            val dateAttrs = getCurrentDate()
            DatePickerDialog(
                requireContext(),
                this,
                dateAttrs.first,
                dateAttrs.second - 1,
                dateAttrs.third
            ).show()
        }

        return binding.root
    }

    private fun getCurrentDate(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        return if (editProfileViewModel.savedDay.value == 0) {
            Triple(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            Triple(
                editProfileViewModel.savedYear.value ?: 0,
                editProfileViewModel.savedMonth.value ?: 0,
                editProfileViewModel.savedDay.value ?: 0
            )
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        editProfileViewModel.setBirthDate(dayOfMonth, month + 1, year)
    }

}