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
import com.example.jangkau.moneyFormatter
import org.koin.android.ext.android.inject

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val bankViewModel : BankAccountViewModel by inject()
    private val userViewModel : UserViewModel by inject()

    private var isBalanceHidden: Boolean = false // Default to hidden
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObserver()


        binding.btnTransfer.setOnClickListener {
            openTransferActivity()
        }


        binding.icon.setOnCheckedChangeListener { _, isChecked ->
            isBalanceHidden = isChecked
            toggleBalanceVisibility()
        }




    }

    private fun setObserver(){
        bankViewModel.showDataBankAcc()
        bankViewModel.state.observe(this){state->
            when(state){
                is State.Error -> {
                    showToast(state.error)

                }
                State.Loading -> {

                }
                is State.Success -> {
                    binding.tv2.text = state.data.ownerName
                    binding.tvSaldo.text = moneyFormatter(state.data.balance.toLong())
                    binding.tvRekening.text = state.data.accountNumber
                }
            }
        }
    }

    private fun toggleBalanceVisibility() {
        isBalanceHidden = !isBalanceHidden
        val balance = bankViewModel.state.value?.let {
            if (it is State.Success) it.data.balance.toLong() else 0L
        } ?: 0L
        updateBalanceVisibility(balance)
    }

    private fun updateBalanceVisibility(balance: Long) {
        if (isBalanceHidden) {
            binding.tvSaldo.text = "********"
        } else {
            binding.tvSaldo.text = moneyFormatter(balance)
        }
    }
}