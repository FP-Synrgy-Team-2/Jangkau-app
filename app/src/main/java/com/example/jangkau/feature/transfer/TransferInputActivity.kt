package com.example.jangkau.feature.transfer

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jangkau.R
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferInputBinding
import com.example.jangkau.databinding.BottomSheetTransferConfirmationBinding
import com.example.jangkau.gone
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

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


        binding.navbar.imgCancel.gone()

        binding.btnNext.setOnClickListener {
            openBottomDialog()
        }

    }

    private fun openBottomDialog(){
        dialog.setContentView(bottomSheetBinding.root)
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()



        bottomSheetBinding.apply {

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