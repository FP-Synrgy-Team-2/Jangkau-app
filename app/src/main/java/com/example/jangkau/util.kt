package com.example.jangkau

import android.app.Activity
import android.view.View
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
import java.text.DecimalFormat

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

fun succesPopUp( text:String ,activity: Activity) {
    AirySnackbar.make(
        source = AirySnackbarSource.ActivitySource(activity),
        type = Type.Custom(R.color.white ),
        attributes = listOf(
            TextAttribute.Text(text = text),
            TextAttribute.TextColor(textColor = R.color.black),
            IconAttribute.Icon(R.drawable.ic_success),
            IconAttribute.IconColor(iconTint = R.color.success),
            RadiusAttribute.Radius(radius = 0.2f),
            SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
            SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
            GravityAttribute.Top,
            AnimationAttribute.FadeInOut,
        ),
    ).show()
}

fun failedPopUp( text:String ,activity: Activity) {
    AirySnackbar.make(
        source = AirySnackbarSource.ActivitySource(activity),
        type = Type.Custom(R.color.white),
        attributes = listOf(
            TextAttribute.Text(text = text),
            TextAttribute.TextColor(textColor = R.color.black),
            IconAttribute.Icon(R.drawable.ic_failed),
            IconAttribute.IconColor(iconTint = R.color.red_custom),
            RadiusAttribute.Radius(radius = 0.4f),
            SizeAttribute.Margin(left = 0, right = 0, unit = SizeUnit.DP),
            SizeAttribute.Padding(top = 12, bottom = 12, right = 8, unit = SizeUnit.DP),
            GravityAttribute.Top,
            AnimationAttribute.FadeInOut,
        ),
    ).show()
}
