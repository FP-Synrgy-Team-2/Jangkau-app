package com.example.jangkau.feature.transfer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.databinding.ActivityTransferInputBinding
import com.example.jangkau.gone

class TransferInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferInputBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navbar.imgCancel.gone()

    }
}