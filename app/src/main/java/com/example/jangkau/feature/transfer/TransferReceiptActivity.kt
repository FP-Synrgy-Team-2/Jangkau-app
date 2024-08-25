package com.example.jangkau.feature.transfer

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferReceiptBinding
import com.example.jangkau.databinding.BottomSheetShareBinding
import com.example.jangkau.formatDate
import com.example.jangkau.gone
import com.example.jangkau.moneyFormatter
import com.example.jangkau.successPopUp
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.viewmodel.TransactionViewModel
import com.example.jangkau.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.android.ext.android.inject
import java.io.OutputStream

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transactionId = intent.getStringExtra("EXTRA_TRANSACTION_ID")

        if (transactionId != null) {
            playSuccessSound()
            transactionViewModel.getTransactionById(transactionId)
            transactionViewModel.transactions.observe(this) { state ->
                when (state) {
                    is State.Error -> {
                        hideLoadingDialog()
                        Log.d("GetTransactionById", "Error : ${state.error}")
                        showToast(state.error)
                    }
                    State.Loading -> {
                        showLoadingDialog()
                        Log.d("GetTransactionById", "Loading")
                    }
                    is State.Success -> {
                        hideLoadingDialog()
                        binding.apply {
                            tvTransactionId.text = state.data.transactionId
                            tvName.text = state.data.beneficiaryName
                            tvRekening.text = state.data.beneficiaryAccount
                            tvDate.text = formatDate(state.data.transactionDate)
                            tvNominal.text = moneyFormatter(state.data.amount.toLong())
                            tvBiayaAdmin.text = moneyFormatter(state.data.adminFee.toLong())
                            tvTransfer.text = moneyFormatter(state.data.amount.toLong())
                            tvCatatan.text = "- ${state.data.note}"
                        }
                    }
                }
            }
        }

        binding.btnBeranda.setOnClickListener {
            openHomeActivity()
            finish()
        }

        binding.btnDownload.setOnClickListener {
            successPopUp("Bukti Transaksi berhasil disimpan di galeri anda", this)
            saveReceiptAsImage()
        }

        binding.btnShare.setOnClickListener {
            openBottomDialog()
        }
    }

    private fun openBottomDialog() {
        dialog.setContentView(bottomSheetBinding.root)
        val behavior = BottomSheetBehavior.from(bottomSheetBinding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        bottomSheetBinding.apply {
            btnTelegram.setOnClickListener {
                shareViaTelegram()
            }

            btnWhatsapp.setOnClickListener {
                shareViaWhatsapp()
            }

            btnInstagram.setOnClickListener {
                shareViaInstagram()
            }

            btnGmail.setOnClickListener {
                shareViaGmail()
            }

            navbar.tvTitlePage.text = getString(R.string.bagikan)
            navbar.imgBackArrow.gone()
            navbar.imgCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }


    private fun shareViaTelegram() {
        val imagePath = saveReceiptAsImage()
        imagePath?.let {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(it))
                setPackage("org.telegram.messenger")
            }
            try {
                startActivity(shareIntent)
            } catch (e: Exception) {
                showToast("Telegram belum terinstall pada device anda")
            }
        } ?: showToast("Failed to save receipt")
    }

    private fun shareViaGmail() {
        val imagePath = saveReceiptAsImage()
        imagePath?.let {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(it))
                putExtra(Intent.EXTRA_SUBJECT, "Receipt Transaction for BCA Transfer")
                setPackage("com.google.android.gm")
            }
            try {
                startActivity(shareIntent)
            } catch (e: Exception) {
                showToast("Gmail belum terinstall pada device anda")
            }
        } ?: showToast("Failed to save receipt")
    }

    private fun shareViaInstagram() {
        val imagePath = saveReceiptAsImage()
        imagePath?.let {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(it))
                setPackage("com.instagram.android")
            }
            try {
                startActivity(shareIntent)
            } catch (e: Exception) {
                showToast("Instagram belum terinstall pada device anda")
            }
        } ?: showToast("Failed to save receipt")
    }


    private fun shareViaWhatsapp() {
        val imagePath = saveReceiptAsImage()
        imagePath?.let {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(it))
                setPackage("com.whatsapp")
            }
            try {
                startActivity(shareIntent)
            } catch (e: Exception) {
                showToast("Whatsapp belum terinstall pada device anda")
            }
        } ?: showToast("Failed to save receipt")
    }

    private fun saveReceiptAsImage(): String? {
        val bitmap = getBitmapFromView(binding.root)
        return saveBitmapToMediaStore(bitmap)
    }

    private fun getBitmapFromView(view: View): Bitmap {
        // Hide buttons
        binding.btnDownload.gone()
        binding.btnShare.gone()
        binding.btnBeranda.gone()

        // Capture view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        binding.btnDownload.visible()
        binding.btnShare.visible()
        binding.btnBeranda.visible()

        return bitmap
    }

    private fun saveBitmapToMediaStore(bitmap: Bitmap): String? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Receipt_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
            uri.toString()
        }
    }

    // Optional: Remove permission handling if targeting API level 29+
    // If targeting API level 29 or higher, you do not need WRITE_EXTERNAL_STORAGE permission.
}