package com.example.jangkau.feature

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.jangkau.LoadingActivity
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityPinInputBinding
import com.example.jangkau.failedPopUp
import com.example.jangkau.gone
import com.example.jangkau.shorten
import com.ygoular.numpadview.NumPadView
import org.koin.ext.clearQuotes

class PinValidationActivity : BaseActivity() {

    companion object {
        const val EXTRA_TARGET_ACTION = "EXTRA_TARGET_ACTION"
        const val EXTRA_PIN = "EXTRA_PIN"
    }

    private lateinit var binding: ActivityPinInputBinding
    private var currentValue: String = ""
    private var targetAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        targetAction = intent.getStringExtra(EXTRA_TARGET_ACTION)

        binding.navbar.imgCancel.gone()
        binding.navbar.tvTitlePage.gone()
        binding.navbar.imgBackArrow.setOnClickListener { finish() }

        binding.firstPinView.isPasswordHidden = true

        setContentDescriptions(binding.numPadView)

        binding.numPadView.setOnInteractionListener(
            onNewValue = {
                val newValue = handleNewValue(it)
                binding.firstPinView.setText(newValue)

//                binding.numPadView.announceForAccessibility("Number $it entered")

                binding.numPadView.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)

                if (newValue.length == 6) {
                    showLoadingActivity(newValue)
                }
            },
            onRightIconClick = {
                binding.firstPinView.setText(handleRightValue())
            },
        )

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(pinValidationReceiver, IntentFilter("RESULT_ACTION"))
    }

    private val pinValidationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val result = intent?.getStringExtra("RESULT")
            val error = intent?.getStringExtra("ERROR")
            when (result) {
                "SUCCESS" -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                "ERROR" -> {
                    resetPinInput()
                    triggerVibrationWithDelay()
                    failedPopUp(error ?: "PIN validation failed", this@PinValidationActivity)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(pinValidationReceiver)
    }

    private fun handleNewValue(value: String): String {
        currentValue += value
        return currentValue
    }

    private fun handleRightValue(): String {
        currentValue = if (currentValue.length > 1) {
            currentValue.shorten()
        } else {
            ""
        }
        return currentValue
    }

    private fun setContentDescriptions(numPadView: NumPadView) {
        val numberIds = arrayOf(
            com.ygoular.numpadview.R.id.pad_number_0,
            com.ygoular.numpadview.R.id.pad_number_1,
            com.ygoular.numpadview.R.id.pad_number_2,
            com.ygoular.numpadview.R.id.pad_number_3,
            com.ygoular.numpadview.R.id.pad_number_4,
            com.ygoular.numpadview.R.id.pad_number_5,
            com.ygoular.numpadview.R.id.pad_number_6,
            com.ygoular.numpadview.R.id.pad_number_7,
            com.ygoular.numpadview.R.id.pad_number_8,
            com.ygoular.numpadview.R.id.pad_number_9
        )

        for ((index, id) in numberIds.withIndex()) {
            val numberButton = numPadView.findViewById<View>(id)
            numberButton.contentDescription = "Number $index"
        }

        val rightIconId = com.ygoular.numpadview.R.id.pad_number_right_icon
        val rightIcon = numPadView.findViewById<View>(rightIconId)
        rightIcon.contentDescription = getString(R.string.tombol_hapus)

        val leftIconId = com.ygoular.numpadview.R.id.pad_number_left_icon
        val leftIcon = numPadView.findViewById<View>(leftIconId)
        leftIcon.visibility = View.GONE
    }

    private fun showLoadingActivity(pin: String) {
        val intent = Intent(this, LoadingActivity::class.java).apply {
            putExtra("PIN", pin)
        }
        startActivity(intent)
    }

    private fun resetPinInput() {
        currentValue = ""
        binding.firstPinView.setText(currentValue)
    }
}
