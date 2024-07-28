package com.example.jangkau.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.model.BankAccount
import com.example.domain.model.SavedAccount
import com.example.jangkau.R
import com.example.jangkau.databinding.NavbarBinding
import com.example.jangkau.feature.PinInputActivity
import com.example.jangkau.feature.auth.LoginActivity
import com.example.jangkau.feature.home.HomeActivity
import com.example.jangkau.feature.mutation.FilterMutationActivity
import com.example.jangkau.feature.mutation.MutationActivity
import com.example.jangkau.feature.transfer.TransferActivity
import com.example.jangkau.feature.transfer.TransferInputActivity
import com.example.jangkau.feature.transfer.TransferInputActivity.Companion.PIN_INPUT_REQUEST_CODE
import com.example.jangkau.feature.transfer.TransferReceiptActivity

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var navbarBinding: NavbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavbar()

    }


    override fun onStart() {
        super.onStart()
            /** logic for session login **/
    }

    private fun setupNavbar() {
        navbarBinding = NavbarBinding.inflate(layoutInflater)
        setContentView(navbarBinding.root)

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


    fun openMutasiActivity(){
        val intent = Intent(this, MutationActivity::class.java)
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

    fun openPinInputActivity(){
        val intent = Intent(this, PinInputActivity::class.java)
        startActivityForResult(intent, PIN_INPUT_REQUEST_CODE)
    }

    fun openTranferReceiptActivity(){
        val intent = Intent(this, TransferReceiptActivity::class.java)
        startActivity(intent)
    }

}