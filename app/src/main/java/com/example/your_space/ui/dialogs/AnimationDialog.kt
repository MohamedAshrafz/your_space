package com.example.your_space.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.your_space.R
import com.google.android.material.textview.MaterialTextView

class AnimationDialog(private val context: Context) : Dialog(context) {
    private val progress_bar: ProgressBar
    private val message_text: MaterialTextView
    private val done_image: LottieAnimationView
    private val failed_image: LottieAnimationView

    init {
        val window = window
        if (window != null) {
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
//            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            window.attributes = params
        }
        val drawable = ContextCompat.getDrawable(context, R.drawable.rounded_background)
        // Set the drawable as the background of the view
        getWindow()!!.setBackgroundDrawable(null)
        getWindow()!!.decorView.background = drawable
        setContentView(R.layout.dialog_animation)

        // find all views
        progress_bar = findViewById(R.id.progressBar)
        done_image = findViewById(R.id.done_image)
        failed_image = findViewById(R.id.failed_image)
        message_text = findViewById(R.id.message_text)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun doneDismiss(message: String?, startDelay: Long, endRunnable: Runnable?) {
        if (message != null) {
            finishDismiss(message, startDelay, endRunnable, R.id.done_image)
        } else {
            finishDismiss("", startDelay, endRunnable, R.id.done_image)
        }
    }

    fun failedDismiss(message: String?, startDelay: Long, endRunnable: Runnable?) {
        if (message != null) {
            finishDismiss(message, startDelay, endRunnable, R.id.failed_image)
        } else {
            finishDismiss("", startDelay, endRunnable, R.id.failed_image)
        }
    }

    private fun finishDismiss(
        message: String,
        startDelay: Long,
        endRunnable: Runnable?,
        resId: Int
    ) {
        if (!isShowing) return
        Handler(Looper.getMainLooper()).postDelayed({
            progress_bar.visibility = View.INVISIBLE
            var animationView: LottieAnimationView? = null
            if (resId == R.id.done_image) {
                animationView = done_image
            } else if (resId == R.id.failed_image) {
                animationView = failed_image
            }
            message_text.text = message
            if (animationView != null) {
                animationView.visibility = View.VISIBLE
                animationView.playAnimation()

                Handler(Looper.getMainLooper()).postDelayed({
                    dismiss()
                    endRunnable?.run()
                }, 2000L)
            } else {
                dismiss()
                endRunnable?.run()
            }
        }, startDelay)
    }

    fun show(message: String?) {
        done_image.visibility = View.GONE
        failed_image.visibility = View.GONE
        message_text.text = message
        show()
    }

    override fun dismiss() {
        super.dismiss()
        // reset ui
        progress_bar.visibility = View.VISIBLE
    }

    companion object {
        private val TAG = AnimationDialog::class.java.simpleName
    }
}
