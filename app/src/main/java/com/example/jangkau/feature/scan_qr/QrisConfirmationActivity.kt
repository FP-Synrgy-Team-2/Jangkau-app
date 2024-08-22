package com.example.jangkau.feature.scan_qr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.jangkau.CurrencyTextWatcher
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityQrisConfirmationBinding
import com.example.jangkau.gone
import com.example.jangkau.parseCurrency
import com.example.jangkau.viewmodel.TransactionViewModel
import com.example.jangkau.visible
import org.koin.android.ext.android.inject

class QrisConfirmationActivity : BaseActivity() {
    private lateinit var binding: ActivityQrisConfirmationBinding
    private val transactionViewModel: TransactionViewModel by inject()

    companion object {
        const val PIN_INPUT_REQUEST_CODE = 1
    }

    private var accountId: String? = null
    private var nominal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrisConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountId = intent.getStringExtra("EXTRA_ACCOUNT_ID")
        val accountNumber = intent.getStringExtra("EXTRA_ACCOUNT_NUMBER")
        val ownerName = intent.getStringExtra("EXTRA_OWNER_NAME")

        binding.cardRekeningTujuan.tvRekeningTujuan.text = "Penerima"
        binding.cardRekeningTujuan.tvNamaPemilik.text = ownerName
        binding.cardRekeningTujuan.tvNomorRekening.text = accountNumber

        binding.navbar.apply {
            tvTitlePage.text = "Detail Transaksi QRIS"
            imgBackArrow.setOnClickListener { finish() }
            imgCancel.gone()
        }

        binding.edtNominal.addTextChangedListener(CurrencyTextWatcher(binding.edtNominal))

        binding.btnNext.setOnClickListener {
            nominal = parseCurrency(binding.textInputLayoutNominal.editText?.text.toString()).toInt()
            if (nominal > 0) {
                openPinInputActivity()
                binding.btnNext.gone()
                binding.progressBar.visible()
            } else {
                showToast("Please enter a valid nominal amount")
            }
        }

        observeTransactionState()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            executeTransferQris()
        }
    }

    private fun executeTransferQris() {
        val accountId = accountId ?: return // Ensure accountId is not null
        transactionViewModel.transferWithQris(accountId, nominal)
    }

    private fun observeTransactionState() {
        transactionViewModel.transactions.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    binding.progressBar.gone()
                    binding.btnNext.visible()
                    showToast(state.error)
                }
                State.Loading -> {
                    Log.d("Transfer", "Loading state")
                }
                is State.Success -> {
                    binding.progressBar.gone()
                    binding.btnNext.visible()
                    openQrisReceiptActivity(state.data.transactionId)
                    finish()
                    Log.d("TransferInputActivity", "Success state: ${state.data}")
                }
            }
        }
    }
}
