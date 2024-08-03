package com.example.jangkau.feature.transfer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.domain.model.SavedAccount
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferInputBinding
import com.example.jangkau.databinding.BottomSheetTransferConfirmationBinding
import com.example.jangkau.gone
import com.example.jangkau.invisible
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

    companion object {
        const val PIN_INPUT_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedAccount = intent.getSerializableExtra("EXTRA_SAVED_ACCOUNT") as? SavedAccount
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

        binding.navbar.imgCancel.gone()

        binding.btnNext.setOnClickListener {
            var namaRekening = savedAccount?.ownerName
            val rekeningTujuan = binding.textInputLayoutRekeningTujuan.editText?.text.toString()
            val nominal = binding.textInputLayoutNominal.editText?.text.toString().toInt()
            val catatan = binding.textInputLayoutCatatan.editText?.text.toString()
            val isSaved = binding.cbSimpanRekening.isChecked

            if (rekeningTujuan.isNotEmpty() && nominal.toString().isNotEmpty() && catatan.isNotEmpty()) {
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
                            val idRekeningPenerima = state.data.accountId ?: ""
                            openBottomDialog(idRekeningPenerima, namaRekening, rekeningTujuan, nominal, catatan, isSaved)
                        }
                    }
                }
            } else {
                showToast("Please fill all the fields")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            openTransferReceiptActivity(transactionId)
        }
    }

    private fun openBottomDialog(idRekeningPenerima: String, namaRekening: String?, rekeningTujuan: String, nominal: Int, catatan: String, isSaved: Boolean) {
        dialog.setContentView(bottomSheetBinding.root)
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        bottomSheetBinding.apply {
            tvName.text = namaRekening
            tvRekening.text = rekeningTujuan
            tvNominal.text = nominal.toString()
            tvCatatan.text = catatan
            btnNext.setOnClickListener {
                bottomSheetBinding.progressBar.visible()
                bottomSheetBinding.btnNext.invisible()
                transactionViewModel.transfer(idRekeningPenerima, nominal, catatan, isSaved)
                transactionViewModel.transactions.observe(this@TransferInputActivity) { state ->
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
                            openPinInputActivity()
                            Log.d("TransferInputActivity", "Success state: ${state.data}")
                        }
                    }
                }
            }

            navbar.imgBackArrow.gone()
            navbar.imgCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}
