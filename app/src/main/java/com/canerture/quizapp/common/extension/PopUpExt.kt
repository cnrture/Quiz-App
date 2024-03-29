package com.canerture.quizapp.common.extension

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.DrawableRes
import com.canerture.quizapp.R
import com.canerture.quizapp.databinding.FullScreenErrorBinding
import com.canerture.quizapp.databinding.PopupErrorBinding

fun Dialog.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun Context.showErrorPopup(
    @DrawableRes iconId: Int,
    title: String,
    dismissListener: (() -> Unit?)? = null,
) {
    Dialog(this).apply {
        val binding = PopupErrorBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        setWidthPercent(75)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setOnCancelListener {
            dismissListener?.invoke()
        }

        with(binding) {
            ivImage.setImageResource(iconId)
            tvMessage.text = title
            btnOkay.setOnClickListener {
                dismiss()
                dismissListener?.invoke()
            }
        }

        show()
    }
}

fun Context.showFullPageErrorPopup(
    @DrawableRes iconId: Int,
    title: String? = null,
    dismissListener: () -> Unit,
) {
    Dialog(this, R.style.BaseDialogStyle).apply {
        val binding = FullScreenErrorBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        with(binding) {
            ivImage.setImageResource(iconId)
            tvMessage.text = title
            btnOkay.setOnClickListener {
                dismiss()
                dismissListener.invoke()
            }
        }

        setCancelable(false)
        setCanceledOnTouchOutside(false)

        show()
    }
}