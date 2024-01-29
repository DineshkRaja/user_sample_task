package com.example.mydatasensetask.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.mydatasensetask.R
import com.example.mydatasensetask.databinding.DialogLottieProgressBinding
import kotlin.math.roundToInt

class LottieProgressDialog(
    context: Context,
    // setting cancelable
    private val isCancel: Boolean,
    // if you want change dialog size input value not null
    private val dialogWidth: Int?,
    private val dialogHeight: Int?,
    // if you want change animation size input value not null
    private val animationViewWidth: Int?,
    private val animationViewHeight: Int?,
    // if you want change animation in samples refer to companion object
    // if you want specific file input file name not null
    private val fileName: String,
    // if you want change title input string not null
    private val title: String?,
    // if you want change title visible input visible value not null
    private val titleVisible: Int? = View.VISIBLE,
    private val okayButtonVisibility: Boolean = false
) :
    Dialog(context) {
    private var mContext: Context = context

    private lateinit var bindingLottie: DialogLottieProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.requestFeature(Window.FEATURE_NO_TITLE)
        bindingLottie = DialogLottieProgressBinding.inflate(layoutInflater)
        val view = bindingLottie.root
        setContentView(view)

        setCancelable(isCancel)

        bindingLottie.lottieContainer.layoutParams.apply {
            dialogWidth?.let {
                width = dpToPx(it)
            }
            dialogHeight?.let {
                height = dpToPx(it)
            }
        }

        bindingLottie.lottieAnimationView.layoutParams.apply {
            animationViewWidth?.let {
                width = it
            }

            animationViewHeight?.let {
                height = it
            }
        }

        bindingLottie.lottieAnimationView.setAnimation(R.raw.loading)

        bindingLottie.titleTextView.apply {
            title?.let {
                text = it
            }
            titleVisible?.let {
                visibility = it
            }
        }

        if (okayButtonVisibility) {
            bindingLottie.titleTextView.visibility = View.GONE
            bindingLottie.okayTextView.visibility = View.VISIBLE
            bindingLottie.okayTextView.setOnClickListener {
                this.dismiss()
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = mContext.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }

    companion object {
        const val LOADING = "loading.json"
    }
}