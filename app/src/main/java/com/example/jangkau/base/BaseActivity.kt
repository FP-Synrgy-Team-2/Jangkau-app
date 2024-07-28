package com.example.jangkau.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.model.BankAccount
import com.example.jangkau.feature.PinInputActivity
import com.example.jangkau.feature.ScanQRActivity
import com.example.jangkau.feature.auth.LoginActivity
import com.example.jangkau.feature.forgot_password.InputEmailActivity
import com.example.jangkau.feature.forgot_password.InputNewPasswordActivity
import com.example.jangkau.feature.forgot_password.InputOtpActivity
import com.example.jangkau.feature.home.HomeActivity
import com.example.jangkau.feature.transfer.TransferActivity
import com.example.jangkau.feature.transfer.TransferInputActivity
import com.example.jangkau.feature.transfer.TransferReceiptActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
            /** logic for session login **/
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

    fun openTransferActivity() {
        val intent = Intent(this, TransferActivity::class.java)
        startActivity(intent)
    }

    fun openTransferInputActivity(savedAccount: BankAccount? = null) {
        val intent = Intent(this, TransferInputActivity::class.java).apply {
            putExtra("EXTRA_SAVED_ACCOUNT", savedAccount)
        }
        startActivity(intent)
    }

//    fun openTransferConfirmationActivity(){
//        val intent = Intent(this, TransferConfirmationActivity::class.java)
//        startActivity(intent)
//    }

    fun openPinInputActivity(){
        val intent = Intent(this, PinInputActivity::class.java)
        startActivity(intent)
    }

    fun openTranferReceiptActivity(){
        val intent = Intent(this, TransferReceiptActivity::class.java)
        startActivity(intent)
    }


    fun openInputEmailActivity(){
        val intent = Intent(this, InputEmailActivity::class.java)
        startActivity(intent)
    }

    fun openInputOtpActivity(){
        val intent = Intent(this, InputOtpActivity::class.java)
        startActivity(intent)
    }

    fun openInputNewPasswordActivity(){
        val intent = Intent(this, InputNewPasswordActivity::class.java)
        startActivity(intent)
    }

    fun openQrisActivity(){
        val intent = Intent(this, ScanQRActivity::class.java)
        startActivity(intent)
    }
}