package com.example.jangkau.feature.scan_qr

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityQrisReceiptBinding

class QrisReceiptActivity : BaseActivity() {
    private lateinit var binding: ActivityQrisReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}