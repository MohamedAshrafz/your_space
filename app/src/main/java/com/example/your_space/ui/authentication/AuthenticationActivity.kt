package com.example.your_space.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.your_space.databinding.ActivityAuthenticationBinding
import com.example.your_space.ui.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {
    companion object {
        const val SIGN_IN_RESULT_CODE = 2030
        const val SIGN_IN_SUCCEEDED_EXTRA = "sign in succeeded"
        const val LOGIN_STATE = "login state"
        const val USER_DATA = "user data"
        const val USER_ID = "user id"
        private val TAG = AuthenticationActivity::class.java.simpleName
    }

    lateinit var binding: ActivityAuthenticationBinding

    private val signInViewModel: SignInViewModel by lazy {
        ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
//        binding.loginButton.setOnClickListener { launchSignupFlow() }

        // If the user was authenticated, send him to RemindersActivity
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            sentToMainActivity()
//        }

        val receiveSP = getSharedPreferences(LOGIN_STATE, MODE_PRIVATE)

        val userId = receiveSP.getString(USER_ID, null)

        if (userId != null) {
            sentToMainActivity()
        }

        signInViewModel.signed.observe(this) { signedVal ->
            Log.e("Auth Activity", signedVal.toString() + signInViewModel.currentUser.value)
            if (signedVal && signInViewModel.currentUser.value != null) {

                val sentSP = getSharedPreferences(LOGIN_STATE, MODE_PRIVATE)
                val editor = sentSP.edit()
                editor.putString(USER_ID, signInViewModel.currentUser.value?.userId)
                editor.apply()

                sentToMainActivity()
                signInViewModel.clearSigned()
            }
        }
    }

    // sending the the user to the reminder list activity and setting an extra to indicate
    // that he is signed in the first time the app launches this activity only
    private fun sentToMainActivity() {
        val startMainActivityIntent =
            Intent(applicationContext, MainActivity::class.java)
//                .putExtra(
//                    USER_DATA, signUpViewModel.currentUser.value
//                )
        startActivity(startMainActivityIntent)
        this.finish()
    }

    private fun launchSignupFlow() {
        // Give users the option to sign in / register with their email or Google account. If users
        // choose to register with their email, they will need to create a password as well.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent.
        // the new way of doing so with the use of registerForActivityResult
        // no need for SIGN_IN_RESULT_CODE
        resultLauncher.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
        )
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val response = IdpResponse.fromResultIntent(result.data)
            if (result.resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                Log.i(
                    TAG,
                    "Successfully signed in user " +
                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
                // navigate to the reminder list fragment
                sentToMainActivity()
            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using
                // the back button. Otherwise check response.getError().getErrorCode() and handle
                // the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
                Toast.makeText(
                    applicationContext,
                    "There was an error in the signing in",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}