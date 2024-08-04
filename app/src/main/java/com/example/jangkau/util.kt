package com.example.jangkau

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.akndmr.library.AirySnackbar
import com.akndmr.library.AirySnackbarSource
import com.akndmr.library.AnimationAttribute
import com.akndmr.library.GravityAttribute
import com.akndmr.library.IconAttribute
import com.akndmr.library.RadiusAttribute
import com.akndmr.library.SizeAttribute
import com.akndmr.library.SizeUnit
import com.akndmr.library.TextAttribute
import com.akndmr.library.Type
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun String.shorten() = substring(0, lastIndex)

fun moneyFormatter(value: Long?, withPrefix: Boolean = true): String {
    val myFormatter = DecimalFormat("#,###.###")
    val formatted = myFormatter.format(value?.toDouble()).replace(",".toRegex(), ".")
    return if (withPrefix) "Rp$formatted" else formatted
}

fun showCustomSnackbar(
    text: String,
    activity: Activity,
    iconRes: Int,
    iconTintRes: Int,
    radius: Float,
    onDismissAction: () -> Unit = {}
) {
    // Get the root view of the activity
    val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)

    // Check if the overlay already exists and remove it
    val existingOverlayView = rootView.findViewById<View>(R.id.overlay_view)
    existingOverlayView?.let {
        rootView.removeView(it)
    }

    // Create the overlay view
    val overlayView = View(activity).apply {
        id = R.id.overlay_view
        setBackgroundColor(Color.parseColor("#80000000")) // semi-transparent black
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        alpha = 0f
    }

    // Add the overlay to the root view
    rootView.addView(overlayView)

    // Animate the overlay to fade in
    overlayView.animate().alpha(1f).setDuration(300).start()

    // Create and show the snackbar
    val snackbar = createSnackbar(activity, text, iconRes, iconTintRes, radius, onDismissAction)
    snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<AirySnackbar>() {
        override fun onShown(transientBottomBar: AirySnackbar?) {
            super.onShown(transientBottomBar)
            // Ensure the overlay is visible when the snackbar is shown
            overlayView.alpha = 1f
        }

        override fun onDismissed(transientBottomBar: AirySnackbar?, event: Int) {
            // Animate the overlay to fade out and remove it
            overlayView.animate().alpha(0f).setDuration(300).withEndAction {
                rootView.removeView(overlayView)
                onDismissAction()
            }.start()
        }
    })

    snackbar.show()
}

private fun createSnackbar(
    activity: Activity,
    text: String,
    iconRes: Int,
    iconTintRes: Int,
    radius: Float,
    onDismissAction: () -> Unit
): AirySnackbar {
    return AirySnackbar.make(
        source = AirySnackbarSource.ActivitySource(activity),
        type = Type.Custom(R.color.white),
        attributes = listOf(
            TextAttribute.Text(text = text),
            TextAttribute.TextColor(textColor = R.color.black),
            IconAttribute.Icon(iconRes),
            IconAttribute.IconColor(iconTint = iconTintRes),
            RadiusAttribute.Radius(radius = radius),
            SizeAttribute.Margin(left = 4, right = 4, unit = SizeUnit.DP),
            SizeAttribute.Padding(top = 15, bottom = 15, right = 10, unit = SizeUnit.DP),
            GravityAttribute.Top,
            AnimationAttribute.FadeInOut,
        )
    )
}

fun successPopUp(text: String, activity: Activity) {
    showCustomSnackbar(
        text = text,
        activity = activity,
        iconRes = R.drawable.ic_success,
        iconTintRes = R.color.success,
        radius = 0.2f
    )
}

fun failedPopUp(text: String, activity: Activity) {
    showCustomSnackbar(
        text = text,
        activity = activity,
        iconRes = R.drawable.ic_failed,
        iconTintRes = R.color.red_custom,
        radius = 0.4f
    )
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        if (date != null) {
            outputFormat.format(date)
        } else {
            dateString
        }
    } catch (e: Exception) {
        dateString
    }
}

