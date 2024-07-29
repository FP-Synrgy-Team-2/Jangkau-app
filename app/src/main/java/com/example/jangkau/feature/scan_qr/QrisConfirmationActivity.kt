package com.example.jangkau.feature.scan_qr

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityQrisConfirmationBinding
import com.example.jangkau.databinding.ActivityQrisReceiptBinding

class QrisConfirmationActivity : BaseActivity() {
    private lateinit var binding : ActivityQrisConfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCardPenerima()

        binding.btnNext.setOnClickListener {
            openPinInputActivity("openQrisReceiptActivity")
        }

    }

    private fun setCardPenerima(){
        binding.cardRekeningTujuan.tvRekeningTujuan.text = "Penerima"
        // Set to user Rekening
    }
}