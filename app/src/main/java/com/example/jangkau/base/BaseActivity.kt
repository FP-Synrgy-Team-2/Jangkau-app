package com.example.jangkau.base

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.model.BankAccount

import com.example.jangkau.LoadingActivity
import com.example.jangkau.R
import com.example.jangkau.databinding.DialogLoadingBinding
import com.example.jangkau.databinding.NavbarBinding
import com.example.jangkau.feature.PinValidationActivity
import com.example.jangkau.feature.scan_qr.ScanQRActivity
import com.example.jangkau.feature.auth.LoginActivity
import com.example.jangkau.feature.forgot_password.InputEmailActivity
import com.example.jangkau.feature.forgot_password.InputNewPasswordActivity
import com.example.jangkau.feature.forgot_password.InputOtpActivity
import com.example.jangkau.feature.home.HomeActivity

import com.example.jangkau.feature.mutation.FilterMutationActivity
import com.example.jangkau.feature.mutation.MutationActivity

import com.example.jangkau.feature.scan_qr.GenerateCodeQRActivity
import com.example.jangkau.feature.scan_qr.QrisConfirmationActivity
import com.example.jangkau.feature.scan_qr.QrisReceiptActivity
import com.example.jangkau.feature.transfer.TransferActivity
import com.example.jangkau.feature.transfer.TransferInputActivity
import com.example.jangkau.feature.transfer.TransferInputActivity.Companion.PIN_INPUT_REQUEST_CODE
import com.example.jangkau.feature.transfer.TransferReceiptActivity
import com.example.jangkau.viewmodel.AuthViewModel
import org.koin.android.ext.android.inject
import java.time.LocalDate

abstract class BaseActivity : AppCompatActivity() {

    private val authViewModel : AuthViewModel by inject()
    private lateinit var navbarBinding: NavbarBinding
    private lateinit var loadingDialog: AlertDialog
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavbar()

    }


    override fun onStart() {
        super.onStart()

    }


    fun playSuccessSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.success)
        mediaPlayer?.start()
    }

    fun showLoadingDialog() {
        val binding = DialogLoadingBinding.inflate(layoutInflater)

        loadingDialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    private fun triggerFailedVibration() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationPattern = longArrayOf(0, 200, 100, 200, 100, 300)
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
        } else {
            val vibrationPattern = longArrayOf(0, 200, 100, 200, 100, 300)
            vibrator.vibrate(vibrationPattern, -1)
        }
    }

    fun triggerVibrationWithDelay(delayMillis: Long = 2000) {
        Handler(Looper.getMainLooper()).postDelayed({
            triggerFailedVibration()  // Trigger the vibration after the delay
        }, delayMillis)
    }
    fun setupNavbar() {
        navbarBinding = NavbarBinding.inflate(layoutInflater)
        navbarBinding.imgBackArrow.setOnClickListener {
            finish()
        }
    }


    fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }


    fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun openHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun openTransferActivity(accountNumber: String?, ownerName: String?, balance : Double) {
        val intent = Intent(this, TransferActivity::class.java).apply {
            putExtra("EXTRA_ACCOUNT_NUMBER", accountNumber)
            putExtra("EXTRA_OWNER_NAME", ownerName)
            putExtra("EXTRA_BALANCE", balance)
        }
        startActivity(intent)
    }

    fun openTransferInputActivity(savedAccount: BankAccount? = null, accountNumber: String? = null, ownerName: String? = null, balance : Double? = null) {
        val intent = Intent(this, TransferInputActivity::class.java).apply {
            putExtra("EXTRA_SAVED_ACCOUNT", savedAccount)
            putExtra("EXTRA_ACCOUNT_NUMBER", accountNumber)
            putExtra("EXTRA_BALANCE", balance)
            putExtra("EXTRA_OWNER_NAME", ownerName)
        }
        startActivity(intent)
    }



    fun openMutasiActivity(fromDate : LocalDate, toDate : LocalDate, fromMutationFilter : Boolean = false){
        val intent = Intent(this, MutationActivity::class.java).apply {
            putExtra("EXTRA_FROM_DATE", fromDate.toString())
            putExtra("EXTRA_TO_DATE", toDate.toString())
            putExtra("EXTRA_FROM_MUTATION_FILTER", fromMutationFilter)
        }
        startActivity(intent)
    }

    fun openMutasiFilterActivity(){
        val intent = Intent(this, FilterMutationActivity::class.java)
        startActivity(intent)
    }

//    fun openTransferConfirmationActivity(){
//        val intent = Intent(this, TransferConfirmationActivity::class.java)
//        startActivity(intent)
//    }

    fun openPinInputActivity() {
        val intent = Intent(this, PinValidationActivity::class.java)
         startActivityForResult(intent, PIN_INPUT_REQUEST_CODE)
//        intent.putExtra(PinInputActivity.EXTRA_TARGET_ACTION, action)
//        startActivity(intent)

    }

    fun openTransferReceiptActivity(transactionId : String){
        val intent = Intent(this, TransferReceiptActivity::class.java).apply {
            putExtra("EXTRA_TRANSACTION_ID", transactionId)
        }
        startActivity(intent)
    }


    fun openInputEmailActivity(){
        val intent = Intent(this, InputEmailActivity::class.java)
        startActivity(intent)
    }

    fun openInputOtpActivity(email : String){
        val intent = Intent(this, InputOtpActivity::class.java).apply {
            putExtra("EXTRA_EMAIL", email)
        }
        startActivity(intent)
    }

    fun openInputNewPasswordActivity(email : String, otp : String){
        val intent = Intent(this, InputNewPasswordActivity::class.java).apply {
            putExtra("EXTRA_OTP", otp)
            putExtra("EXTRA_EMAIL", email)
        }
        startActivity(intent)
    }

    fun openQrisActivity(){
        val intent = Intent(this, ScanQRActivity::class.java)
        startActivity(intent)
    }

    fun openGenerateCodeActivity(){
        val intent = Intent(this, GenerateCodeQRActivity::class.java)
        startActivity(intent)
    }

    fun openQrisReceiptActivity(transactionId : String){
        val intent = Intent(this, QrisReceiptActivity::class.java).apply {
            putExtra("EXTRA_TRANSACTION_ID", transactionId)
        }
        startActivity(intent)
    }

    fun openQrisConfirmationActivity(accountNumber: String?, accountId : String?, ownerName: String?){
        val intent = Intent(this, QrisConfirmationActivity::class.java).apply {
            putExtra("EXTRA_ACCOUNT_NUMBER", accountNumber)
            putExtra("EXTRA_ACCOUNT_ID", accountId)
            putExtra("EXTRA_OWNER_NAME", ownerName)
        }
        startActivity(intent)
    }


    fun openLoadingActivity(username: String, password: String) {
        val intent = Intent(this, LoadingActivity::class.java).apply {
            putExtra("USERNAME", username)
            putExtra("PASSWORD", password)
        }
        startActivity(intent)
    }
}