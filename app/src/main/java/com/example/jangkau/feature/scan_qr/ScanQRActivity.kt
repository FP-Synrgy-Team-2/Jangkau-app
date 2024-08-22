package com.example.jangkau.feature.scan_qr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityScanQractivityBinding
import com.example.jangkau.viewmodel.BankAccountViewModel
import org.koin.android.ext.android.inject

class ScanQRActivity : BaseActivity() {

    private lateinit var binding: ActivityScanQractivityBinding
    private lateinit var codeScanner: CodeScanner
    private val bankViewModel: BankAccountViewModel by inject()
    private var isFlashEnabled = false
    private val cameraRequestCode = 101

    companion object {
        const val PIN_INPUT_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codeScanner = CodeScanner(this, binding.scannerView)
        setupScanner()

        binding.btnBack.setOnClickListener { finish() }

        binding.btnGenerateCode.setOnClickListener { openPinInputActivity() }

        binding.btnFlash.setOnClickListener {
            isFlashEnabled = !isFlashEnabled
            codeScanner.isFlashEnabled = isFlashEnabled
            updateFlashButtonIcon()
        }

        checkPermissions()

        bankViewModel.state.observe(this) { state ->
            when (state) {
                is State.Error -> showToast("Error: ${state.error}")
                State.Loading -> Log.d("ScanQR", "Loading...")
                is State.Success -> showToast(state.data.accountNumber)
            }
        }
    }

    private fun setupScanner() {
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                bankViewModel.showDataBankAccByScanQr(it.text)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            openGenerateCodeActivity()
        }
    }

    private fun updateFlashButtonIcon() {
        val flashIcon = if (isFlashEnabled) {
            R.drawable.ic_flash_enabled
        } else {
            R.drawable.ic_flash_disabled
        }
        binding.btnFlash.setImageResource(flashIcon)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequestCode
            )
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            finish()
        }
    }

    private fun startCamera() {
        codeScanner.startPreview()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
