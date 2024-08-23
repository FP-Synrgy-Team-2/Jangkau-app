package com.example.jangkau.feature.home

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.jangkau.ErrorActivity
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityHomeBinding
import com.example.jangkau.databinding.DialogLogoutConfirmationBinding
import com.example.jangkau.gone
import com.example.jangkau.viewmodel.UserViewModel
import com.example.jangkau.moneyFormatter
import com.example.jangkau.successPopUp
import com.example.jangkau.viewmodel.AuthViewModel
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.visible
import org.koin.android.ext.android.inject
import java.time.LocalDate

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val bankViewModel : BankAccountViewModel by inject()
    private val authViewModel : AuthViewModel by inject()

    private var isBalanceHidden: Boolean = false // Default to hidden
    private var accountNumber = ""
    private var ownerName = ""
    private var balance = 0.0
    private var doubleBackToExitPressedOnce = false // Track back button presses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObserver()

        binding.btnTransfer.setOnClickListener {
            openTransferActivity(accountNumber, ownerName, balance)
        }

        binding.btnMutasi.setOnClickListener {
            val fromDate = LocalDate.now().plusDays(1)
            val toDate = fromDate.minusDays(14)
            openMutasiActivity(fromDate, toDate)
        }

        binding.icon.setOnCheckedChangeListener { _, isChecked ->
            isBalanceHidden = isChecked
            toggleBalanceVisibility()
        }

        binding.btnGenerateCode.setOnClickListener {
            openQrisActivity()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            // Close the app
            finishAffinity()
        } else {
            this.doubleBackToExitPressedOnce = true
            showToast("Tekan sekali lagi untuk keluar")

            // Reset the flag after 2 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    private fun setObserver() {
        bankViewModel.showDataBankAcc()
        bankViewModel.state.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    val intent = Intent(this, ErrorActivity::class.java)
                    intent.putExtra("ERROR_MESSAGE", state.error)
                    startActivity(intent)
                }
                State.Loading -> {
                    binding.main.gone()
                }
                is State.Success -> {
                    binding.main.visible()
                    binding.tv2.text = state.data.ownerName
                    binding.tvSaldo.text = moneyFormatter(state.data.balance?.toLong())
                    binding.tvRekening.text = state.data.accountNumber
                    accountNumber = state.data.accountNumber
                    ownerName = state.data.ownerName
                    balance = state.data.balance ?: 0.0
                    binding.btnCopy.setOnClickListener {
                        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val accountNumber = state.data.accountNumber
                        val clipData = android.content.ClipData.newPlainText("Account Number", accountNumber)
                        clipboardManager.setPrimaryClip(clipData)
                        successPopUp("Nomor Rekening berhasil disalin!", this)
                    }
                }
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        val binding = DialogLogoutConfirmationBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        binding.btnYes.setOnClickListener {
            authViewModel.logout()
            openLoginActivity()
            finish()
            dialog.dismiss()
        }

        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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