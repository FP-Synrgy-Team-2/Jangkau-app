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
import com.example.domain.model.BankAccount
import com.example.domain.model.SavedAccount
import com.example.jangkau.ErrorActivity
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
    private var accountId: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codeScanner = CodeScanner(this, binding.scannerView)
        setupScanner()

        binding.btnBack.setOnClickListener { finish() }

        binding.btnGenerateCode.setOnClickListener { openGenerateCodeActivity() }

        binding.btnFlash.setOnClickListener {
            isFlashEnabled = !isFlashEnabled
            codeScanner.isFlashEnabled = isFlashEnabled
            updateFlashButtonIcon()
        }

        checkPermissions()

        bankViewModel.state.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    hideLoadingDialog()
                    showToast("Error: ${state.error}")
                }
                State.Loading -> {
                    showLoadingDialog()
                }
                is State.Success -> {
                    hideLoadingDialog()
                    if(state.data.type  == "User"){
                        bankViewModel.showDataBankAcc()
                        bankViewModel.state.observe(this) { stateOwner ->
                            when (stateOwner) {
                                is State.Error -> {
                                    val intent = Intent(this, ErrorActivity::class.java)
                                    intent.putExtra("ERROR_MESSAGE", stateOwner.error)
                                    startActivity(intent)
                                }
                                State.Loading -> {
                                    Log.d("QrisConfirmation", "Loading state")
                                }
                                is State.Success -> {
                                    openTransferInputActivity(
                                        BankAccount(
                                            accountNumber = state.data.accountNumber,
                                            accountId = state.data.accountId,
                                            ownerName = state.data.ownerName
                                        ),
                                        accountNumber = stateOwner.data.accountNumber,
                                        ownerName = stateOwner.data.ownerName,
                                        balance = stateOwner.data.balance
                                    )

                                }
                            }
                        }
                    }else{
                        openQrisConfirmationActivity(
                            accountNumber = state.data.accountNumber,
                            accountId = state.data.accountId,
                            ownerName = state.data.ownerName
                        )
                    }
                }
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
