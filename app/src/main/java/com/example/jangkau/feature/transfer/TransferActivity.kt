package com.example.jangkau.feature.transfer

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.jangkau.databinding.ActivityTransferBinding

class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navbar.imgCancel.visibility = View.GONE



    }
}