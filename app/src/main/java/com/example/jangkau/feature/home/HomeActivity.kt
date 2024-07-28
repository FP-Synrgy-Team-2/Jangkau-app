package com.example.jangkau.feature.home

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityHomeBinding
import com.example.jangkau.viewmodel.UserViewModel
import com.example.jangkau.moneyFormatter
import com.example.jangkau.successPopUp
import com.example.jangkau.viewmodel.BankAccountViewModel
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
                    binding.tvSaldo.text = moneyFormatter(state.data.balance?.toLong())
                    binding.tvRekening.text = state.data.accountNumber
                    binding.btnCopy.setOnClickListener {
                        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val accountNumber = state.data.accountNumber
                        val clipData = android.content.ClipData.newPlainText("Account Number", accountNumber)
                        clipboardManager.setPrimaryClip(clipData)
                        successPopUp("Nomor Rekening berhasil disalin!                    ", this)
                    }
                }
            }
        }
    }

    private fun toggleBalanceVisibility() {
        val balance = bankViewModel.state.value?.let {
            if (it is State.Success) it.data.balance?.toLong() else 0L
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