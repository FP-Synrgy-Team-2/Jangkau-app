package com.example.jangkau.feature.mutation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityMutationBinding

class MutationActivity : BaseActivity() {
    private lateinit var binding: ActivityMutationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFilter.setOnClickListener {
            openMutasiFilterActivity()
        }


    }
}