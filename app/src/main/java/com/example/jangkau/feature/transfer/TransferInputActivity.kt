package com.example.jangkau.feature.transfer

import android.os.Bundle
import android.view.View
import com.example.domain.model.SavedAccount
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferInputBinding
import com.example.jangkau.databinding.BottomSheetTransferConfirmationBinding
import com.example.jangkau.gone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.jangkau.visible

class TransferInputActivity : BaseActivity() {

    private lateinit var binding: ActivityTransferInputBinding

    private val bottomSheetBinding: BottomSheetTransferConfirmationBinding by lazy {
        BottomSheetTransferConfirmationBinding.inflate(layoutInflater)
    }

    private val dialog: BottomSheetDialog by lazy {
        BottomSheetDialog(
            this, R.style.AppTheme_BottomSheetDialog
        )
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
            binding.cbSimpanRekening.gone()
            binding.textView.gone()
        }

        binding.navbar.imgCancel.gone()

        // need to change the logic
        binding.btnNext.setOnClickListener {
            val namaRekening = savedAccount?.ownerName
            val rekeningTujuan =  binding.textInputLayoutRekeningTujuan.editText?.text.toString()
            val nominal = binding.textInputLayoutNominal.editText?.text.toString()
            val catatan = binding.textInputLayoutCatatan.editText?.text.toString()

            if (rekeningTujuan.isNotEmpty()  || nominal.isNotEmpty() || catatan.isNotEmpty()  ){
                openBottomDialog(namaRekening, rekeningTujuan, nominal, catatan)
            }
        }

    }

    private fun openBottomDialog(namaRekening: String?, rekeningTujuan: String, nominal: String, catatan: String) {
        dialog.setContentView(bottomSheetBinding.root)
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        // should use if else condition when namaRekening = null, will collect data from api based on accountNumber
        bottomSheetBinding.apply {
            tvName.text = namaRekening
            tvRekening.text = rekeningTujuan
            tvNominal.text = nominal
            tvCatatan.text = catatan
            btnNext.setOnClickListener {
                openPinInputActivity()
            }

            navbar.imgBackArrow.gone()
            navbar.imgCancel.setOnClickListener {
                dialog.dismiss()
            }

        }



    }
}