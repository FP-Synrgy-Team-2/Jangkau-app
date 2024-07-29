package com.example.jangkau.feature

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityPinInputBinding
import com.example.jangkau.gone
import com.example.jangkau.shorten
import com.example.jangkau.viewmodel.AuthViewModel
import com.ygoular.numpadview.NumPadView
import org.koin.android.ext.android.inject


class PinInputActivity : BaseActivity() {

    companion object {
        const val EXTRA_TARGET_ACTION = "EXTRA_TARGET_ACTION"
    }

    private lateinit var binding: ActivityPinInputBinding
    private val authViewModel : AuthViewModel by inject()
    private var currentValue: String = ""
    private var targetAction: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        targetAction = intent.getStringExtra(EXTRA_TARGET_ACTION)


        binding.navbar.imgCancel.gone()
        binding.navbar.tvTitlePage.gone()

        binding.firstPinView.isPasswordHidden = true


        // need to be tested for talkback
        setContentDescriptions(binding.numPadView)


        binding.numPadView.setOnInteractionListener(
            onNewValue = {
                val newValue = handleNewValue(it)
                binding.firstPinView.setText(newValue)
                if (newValue.length == 6) {
                    showToast(newValue)
                    authViewModel.validatePin(newValue)
                }
            },
            onRightIconClick = {
                binding.firstPinView.setText(handleRightValue())
            },
        )

        authViewModel.pinValidated.observe(this){state->
            if (state){
// <<<<<<< transfer
//                 setResult(Activity.RESULT_OK)
//                 finish()
// =======
                handleNavigation()
            }else{
                showToast("Pin tidak valid")
            }
        }

    }

    // NEED TO BE CLEANED
    private fun handleNewValue(value: String): String {
        currentValue += value
        return currentValue
    }

    private fun handleRightValue(): String {
        currentValue = if (currentValue.length > 1) { currentValue.shorten() } else { "" }
        return currentValue
    }

    private fun handleNavigation() {
        when (targetAction) {
            "openTransferReceipt" -> openTranferReceiptActivity()
            "openGenerateCode" -> openGenerateCodeActivity()
            "openQrisReceiptActivity" -> openQrisReceiptActivity()
            else -> {
                // Default action or close activity
                finish()
            }
        }
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

}