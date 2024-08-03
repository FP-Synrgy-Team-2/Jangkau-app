package com.example.jangkau.feature.scan_qr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityQrisConfirmationBinding

class QrisConfirmationActivity : BaseActivity() {
    private lateinit var binding : ActivityQrisConfirmationBinding

    companion object {
        const val PIN_INPUT_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCardPenerima()

        binding.btnNext.setOnClickListener {
            openPinInputActivity()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            openQrisReceiptActivity()
        }
    }

    private fun setCardPenerima(){
        binding.cardRekeningTujuan.tvRekeningTujuan.text = "Penerima"
        // Set to user Rekening
    }
}