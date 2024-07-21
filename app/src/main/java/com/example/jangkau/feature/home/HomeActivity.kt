package com.example.jangkau.feature.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityHomeBinding
import com.example.jangkau.feature.auth.UserViewModel
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userViewModel : UserViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTransfer.setOnClickListener {
            openTransferActivity()
        }

        setObserver()




    }

    private fun setObserver(){
        userViewModel.getUser()
        userViewModel.state.observe(this){state->
            when(state){
                is State.Error -> {
                    showToast(state.error)

                }
                State.Loading -> {

                }
                is State.Success -> {
                    binding.tv2.text = state.data.fullname
                }
            }
        }

    }
}