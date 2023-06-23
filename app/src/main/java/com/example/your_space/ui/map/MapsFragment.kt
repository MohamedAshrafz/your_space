package com.example.your_space.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.your_space.BuildConfig
import com.example.your_space.R
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.repository.AppRepository
import com.example.your_space.ui.MainActivity
import com.example.your_space.ui.ourspaces.OurSpacesViewModel
import com.example.your_space.ui.rooms.RoomsFragmentArgs
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MapsFragment : Fragment() {

    lateinit var map: GoogleMap

    private val lat = MutableLiveData(0.0)
    private val lng = MutableLiveData(0.0)
    private val setLocation = MutableLiveData(false)

    // for saving the state of error_snackbar to dismiss it later when the location is enabled
    var locationErrorSnackbarRef: Snackbar? = null

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        checkForPermissions()

        setLocation.observe(viewLifecycleOwner) {
            if (setLocation.value == true) {
                val sydney = LatLng(lat.value!!, lng.value!!)
                googleMap.addMarker(
                    MarkerOptions().position(sydney).title("Selected working space location")
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            sydney.latitude,
                            sydney.longitude
                        ), 12.0f
                    )
                )
                setLocation.value = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spaceId = MapsFragmentArgs.fromBundle(requireArguments()).selectedSpaceId

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        if ((requireActivity() as MainActivity).locationErrorSnackbar != null) {
            locationErrorSnackbarRef =
                (requireActivity() as MainActivity).locationErrorSnackbar
        }


        val repository = AppRepository.getInstance(requireActivity().applicationContext)

        lifecycleScope.launch {
            val latLongList =
                repository.mySession?.let { NetworkServices.getLatLongForSpaceId(spaceId, it) }

            lat.value = latLongList?.get(0)
            lng.value = latLongList?.get(1)
            setLocation.value = true
        }
    }

    override fun onStart() {
        super.onStart()

        if (::map.isInitialized) {
            checkForPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationErrorSnackbarRef != null) {
            (requireActivity() as MainActivity).locationErrorSnackbar =
                locationErrorSnackbarRef
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //  code to check that the user turned on their device location on
        //  if they didn't ask for it
        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
            if (resultCode == Activity.RESULT_OK) {
                // if the user approved to turn on the location dismiss the old error snackbar
                locationErrorSnackbarRef?.dismiss()
                locationErrorSnackbarRef = null
            }
            checkDeviceLocationSettings(false)
        }
    }

    /**
     * In all cases, we need to have the location permission.
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // the code to handle the result of the user's permission

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED
        ) {
            // showing a snackbar if the user didn't approve to give the permission
            Snackbar.make(
                requireView(),
                getString(R.string.error_permission_needed),
                Snackbar.LENGTH_LONG
            )
                // adding action to go to the settings to allow location permission
                .setAction("Settings") {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        } else {
            // Set the app to MyLocationEnabled
            map.isMyLocationEnabled = true
            // check if the location is on after getting the permissions
            checkDeviceLocationSettings()
        }
    }

    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    @SuppressLint("MissingPermission")
    private fun checkForPermissions() {
        if (foregroundLocationPermissionApproved()) {
            // Set the app to MyLocationEnabled
            map.isMyLocationEnabled = true
            // check if the location is on after as we already have the permission to use it
            checkDeviceLocationSettings()
        } else {
            // asking for foreground permission if not granted
            requestForegroundLocationPermissions()
        }
    }

    /**
     *  Determines whether the app has the appropriate permissions across Android 10+ and all other
     *  Android versions.
     */
    @TargetApi(29)
    private fun foregroundLocationPermissionApproved(): Boolean {

        // checking for foreground permission using checkSelfPermission
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     *  Uses the Location Client to check the current state of location settings, and gives the user
     *  the opportunity to turn on location services within our app.
     */
    private fun checkDeviceLocationSettings(resolve: Boolean = true) {
        // the add code to check that the device's location is on
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask =
            settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            // if the location is off but we can resolve that try to resolve
            if ((exception is ResolvableApiException) && resolve) {
                try {
                    startIntentSenderForResult(
                        exception.resolution.intentSender,
                        REQUEST_TURN_DEVICE_LOCATION_ON,
                        null, 0, 0, 0, null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                // if the user dismissed turning the location on show a snackbar with the information
                locationErrorSnackbarRef = Snackbar.make(
                    requireView(),
                    getString(R.string.Asking_to_enable_the_location),
                    Snackbar.LENGTH_LONG
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettings()
                }
                locationErrorSnackbarRef?.show()
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                locationErrorSnackbarRef?.dismiss()
                locationErrorSnackbarRef = null
            }
        }
    }

    /**
     *  Requests ACCESS_FINE_LOCATION
     */
    @TargetApi(29)
    private fun requestForegroundLocationPermissions() {

        // the add code to request foreground permissions
        if (foregroundLocationPermissionApproved())
            return

        // asking for the fine location
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }

}

// random unique values for requests result codes
private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29

// Class TAG and indices of the permissions
private val TAG = MapsFragment::class.java.simpleName
private const val LOCATION_PERMISSION_INDEX = 0