package com.example.jangkau.feature.transfer

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferReceiptBinding
import com.example.jangkau.databinding.BottomSheetShareBinding
import com.example.jangkau.gone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

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