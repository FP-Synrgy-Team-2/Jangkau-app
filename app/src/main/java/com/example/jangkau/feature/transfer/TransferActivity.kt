package com.example.jangkau.feature.transfer

import android.os.Bundle
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferBinding
import com.example.jangkau.gone

class TransferActivity : BaseActivity() {

    private lateinit var binding: ActivityTransferBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navbar.imgCancel.gone()

        binding.btnInputBaru.setOnClickListener {
            openTransferInputActivity()
        }



    }
}