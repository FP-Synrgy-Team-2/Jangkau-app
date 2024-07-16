package com.example.jangkau.feature.transfer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferReceiptBinding

class TransferReceiptActivity : BaseActivity() {

    private lateinit var binding: ActivityTransferReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBeranda.setOnClickListener {
            openHomeActivity()
        }

    }
}