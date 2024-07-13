package com.example.jangkau.feature

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityPinInputBinding

class PinInputActivity : BaseActivity() {

    private lateinit var binding: ActivityPinInputBinding
    private var currentValue: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinInputBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.firstPinView.isPasswordHidden = true

        binding.numPadView.setOnInteractionListener(
           onNewValue = {
               binding.firstPinView .setText(handleNewValue(it))
           }
        )

    }

    private fun handleNewValue(value: String): String {
        currentValue = "${if (currentValue != "0") currentValue else ""}$value"
        return currentValue
    }
}