package com.example.jangkau.feature.transfer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.domain.model.BankAccount
import com.example.domain.model.SavedAccount
import com.example.jangkau.CurrencyTextWatcher
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferInputBinding
import com.example.jangkau.databinding.BottomSheetTransferConfirmationBinding
import com.example.jangkau.gone
import com.example.jangkau.invisible
import com.example.jangkau.moneyFormatter
import com.example.jangkau.parseCurrency
import com.example.jangkau.visible
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.ext.android.inject

class TransferInputActivity : BaseActivity() {

    private lateinit var binding: ActivityTransferInputBinding
    private val bankViewModel: BankAccountViewModel by inject()
    private val transactionViewModel: TransactionViewModel by inject()

    private var transactionId = ""

    private val bottomSheetBinding: BottomSheetTransferConfirmationBinding by lazy {
        BottomSheetTransferConfirmationBinding.inflate(layoutInflater)
    }

    private val dialog: BottomSheetDialog by lazy {
        BottomSheetDialog(
            this, R.style.AppTheme_BottomSheetDialog
        )
    }

    private lateinit var idRekeningPenerima: String
    private lateinit var namaRekening: String
    private lateinit var rekeningTujuan: String
    private var nominal: Int = 0
    private lateinit var catatan: String
    private var isSaved: Boolean = false
    private var balance : Double = 0.0

    companion object {
        const val PIN_INPUT_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navbar.imgCancel.gone()
        binding.navbar.imgBackArrow.setOnClickListener {
            finish()
        }

        val accountNumber = intent.getStringExtra("EXTRA_ACCOUNT_NUMBER")
        val ownerName = intent.getStringExtra("EXTRA_OWNER_NAME")
        balance = intent.getDoubleExtra("EXTRA_BALANCE", 0.0)

        if (accountNumber != null && ownerName != null) {
            binding.cardRekeningSumber.tvNomorRekening.text = accountNumber
            binding.cardRekeningSumber.tvJenisRekening.text = ownerName
        }

        val savedAccount = intent.getSerializableExtra("EXTRA_SAVED_ACCOUNT") as? BankAccount
        if (savedAccount != null) {
            binding.cardRekeningTujuan.cardRekeningTujuan.visible()
            binding.cardRekeningTujuan.tvNomorRekening.text = savedAccount.accountNumber
            binding.cardRekeningTujuan.tvNamaPemilik.text = savedAccount.ownerName

            binding.edtRekeningTujuan.gone()
            binding.edtRekeningTujuan.setText(savedAccount.accountNumber)
            binding.cbSimpanRekening.isChecked = false
            binding.cbSimpanRekening.gone()
            binding.textView.gone()
        }

        binding.textInputLayoutCatatan.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 25) {
                    binding.textInputLayoutCatatan.error = "Catatan tidak bisa melebihi 25 karakter"
                } else {
                    binding.textInputLayoutCatatan.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.navbar.imgCancel.gone()

        binding.edtNominal.addTextChangedListener(CurrencyTextWatcher(binding.edtNominal))

        binding.btnNext.setOnClickListener {
            namaRekening = savedAccount?.ownerName ?: ""
            rekeningTujuan = binding.textInputLayoutRekeningTujuan.editText?.text.toString()
            nominal = parseCurrency(binding.textInputLayoutNominal.editText?.text.toString()).toInt()
            catatan = binding.textInputLayoutCatatan.editText?.text.toString()
            isSaved = binding.cbSimpanRekening.isChecked

            if (rekeningTujuan.isNotEmpty() && nominal > 0) {
                if (nominal > balance) {
                    binding.textInputLayoutNominal.error = "Saldo anda tidak cukup"
                    return@setOnClickListener
                }

                if (rekeningTujuan == accountNumber){
                    binding.textInputLayoutRekeningTujuan.error = "Rekening tujuan tidak boleh sama dengan rekening sumber"
                    return@setOnClickListener
                }


                binding.progressBar.visible()
                binding.btnNext.gone()

                bankViewModel.searchDataBankByAccNumber(rekeningTujuan)
                bankViewModel.state.observe(this) { state ->
                    when (state) {
                        is State.Error -> {
                            binding.progressBar.gone()
                            binding.btnNext.visible()
                            Log.e("BottomSheet", "Error: ${state.error}")
                            showToast(state.error)
                        }
                        State.Loading -> {
                            Log.d("BottomSheet", "Loading")
                        }
                        is State.Success -> {
                            binding.progressBar.gone()
                            binding.btnNext.visible()
                            Log.d("BottomSheet", "Success: ${state.data}")
                            namaRekening = state.data.ownerName
                            idRekeningPenerima = state.data.accountId ?: ""
                            openBottomDialog(accountNumber, namaRekening, rekeningTujuan, nominal, catatan, isSaved)
                        }
                    }
                }
            } else {
                showToast("Silahkan isi semua form terlebih dahulu")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            executeTransfer()
        }else{
            bottomSheetBinding.progressBar.gone()
            bottomSheetBinding.btnNext.visible()
        }
    }

    private fun openBottomDialog(accountNumber: String?, namaRekening: String?, rekeningTujuan: String, nominal: Int, catatan: String, isSaved: Boolean) {
        dialog.setContentView(bottomSheetBinding.root)
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        bottomSheetBinding.apply {
            tvName.text = namaRekening
            tvRekening.text = rekeningTujuan
            tvLabelRekening.text = accountNumber
            tvNominal.text = moneyFormatter(nominal.toLong())
            tvCatatan.text = catatan
            tvBiayaAdmin.text = moneyFormatter(0)
            tvTransfer.text = moneyFormatter(nominal.toLong() + 0)
            btnNext.setOnClickListener {
                openPinInputActivity()
            }

            navbar.imgBackArrow.gone()
            navbar.imgCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun executeTransfer() {
        bottomSheetBinding.progressBar.visible()
        bottomSheetBinding.btnNext.invisible()
        transactionViewModel.transfer(idRekeningPenerima, nominal, catatan, isSaved)
        transactionViewModel.transactions.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    bottomSheetBinding.progressBar.gone()
                    bottomSheetBinding.btnNext.visible()
                    showToast(state.error)
                }
                State.Loading -> {
                    Log.d("Transfer", "Loading state")  // Debug log
                }
                is State.Success -> {
                    bottomSheetBinding.progressBar.gone()
                    bottomSheetBinding.btnNext.visible()
                    transactionId = state.data.transactionId
                    openTransferReceiptActivity(transactionId)
                    Log.d("TransferInputActivity", "Success state: ${state.data}")
                }
            }
        }
    }
}
