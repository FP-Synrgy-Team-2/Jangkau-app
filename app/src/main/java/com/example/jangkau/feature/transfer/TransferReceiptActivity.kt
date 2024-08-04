package com.example.jangkau.feature.transfer

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferReceiptBinding
import com.example.jangkau.databinding.BottomSheetShareBinding
import com.example.jangkau.gone
import com.example.jangkau.moneyFormatter
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.ext.android.inject

class TransferReceiptActivity : BaseActivity() {

    private lateinit var binding: ActivityTransferReceiptBinding

    private val bottomSheetBinding: BottomSheetShareBinding by lazy {
        BottomSheetShareBinding.inflate(layoutInflater)
    }

    private val dialog : BottomSheetDialog by lazy {
        BottomSheetDialog(
            this, R.style.AppTheme_BottomSheetDialog
        )
    }

    private val transactionViewModel : TransactionViewModel by inject()
    private val bankViewModel : BankAccountViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transactionId = intent.getStringExtra("EXTRA_TRANSACTION_ID")

        if (transactionId != null){
            transactionViewModel.getTransactionById(transactionId)
            transactionViewModel.transactions.observe(this){state->
                when(state){
                    is State.Error -> {
                        Log.d("GetTransactionById", "Error : ${state.error}")
                        showToast(state.error)
                    }
                    State.Loading -> {
                        Log.d("GetTransactionById", "Loading")
                    }
                    is State.Success -> {
                        bankViewModel
                        binding.apply {
                            tvTransactionId.text = state.data.transactionId
                            tvName.text = state.data.beneficiaryName
                            tvRekening.text = state.data.beneficiaryAccount
                            tvDate.text = state.data.transactionDate
                            tvNominal.text = moneyFormatter(state.data.amount.toLong())
                            tvBiayaAdmin.text = moneyFormatter(state.data.adminFee.toLong())
                            tvCatatan.text = "- ${state.data.note}"
                        }
                    }
                }
            }
        }

        binding.btnBeranda.setOnClickListener {
            openHomeActivity()
        }


        binding.btnShare.setOnClickListener {
            openBottomDialog()
        }

    }

    private fun openBottomDialog(){

        dialog.setContentView(bottomSheetBinding.root)
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        bottomSheetBinding.apply {

            btnTelegram.setOnClickListener {
                // open Telegram
            }

            btnWhatsapp.setOnClickListener {
                // open Whatsapp Share
            }

            btnInstagram.setOnClickListener {
                // open Instagram
            }

            btnGmail.setOnClickListener {
                // open Gmail
            }


            navbar.tvTitlePage.text = getString(R.string.bagikan)
            navbar.imgBackArrow.gone()
            navbar.imgCancel.setOnClickListener {
                dialog.dismiss()
            }


        }
    }
}