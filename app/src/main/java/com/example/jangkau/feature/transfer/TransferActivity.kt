package com.example.jangkau.feature.transfer

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.BankAccount
import com.example.domain.model.SavedAccount
import com.example.jangkau.ListState
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferBinding
import com.example.jangkau.gone
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.visible
import org.koin.android.ext.android.inject

class TransferActivity : BaseActivity(), AdapterAccountSaved.OnItemClickListener {

    private lateinit var binding: ActivityTransferBinding
    private lateinit var accountAdapter: AdapterAccountSaved
    private val bankViewModel: BankAccountViewModel by inject()

    private var savedAccount: List<BankAccount> = emptyList()
    private var accountNumber: String? = null
    private var ownerName: String? = null
    private var balance : Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountNumber = intent.getStringExtra("EXTRA_ACCOUNT_NUMBER")
        ownerName = intent.getStringExtra("EXTRA_OWNER_NAME")
        balance = intent.getDoubleExtra("EXTRA_BALANCE", 0.0)

        setObserver()

        binding.navbar.imgCancel.gone()
        binding.navbar.imgBackArrow.setOnClickListener {
            finish()
        }

        binding.btnInputBaru.setOnClickListener {
            openTransferInputActivity(
                ownerName = ownerName,
                accountNumber = accountNumber,
                balance = balance
            )
        }
    }

    override fun onItemClick(savedAccount: BankAccount) {
        openTransferInputActivity(
            savedAccount = savedAccount,
            accountNumber = accountNumber,
            ownerName = ownerName
        )
    }

    private fun setObserver() {
        bankViewModel.showSavedBankAcc()
        bankViewModel.savedBankAcc.observe(this) { state ->
            when (state) {
                is State.Error -> {
                    showToast(state.error)
                }
                State.Loading -> {
                    // Handle loading state if necessary
                }
                is State.Success -> {
                    when (val data = state.data) {
                        ListState.Empty -> {
                            binding.rvRekeningTersimpan.gone()
                            binding.imgEmptySavedAccount.visible()
                        }
                        is ListState.Success -> {
                            accountAdapter = AdapterAccountSaved(data.data.toMutableList(), this)
                            savedAccount = data.data.toMutableList()

                            binding.rvRekeningTersimpan.apply {
                                layoutManager = LinearLayoutManager(this@TransferActivity)
                                adapter = accountAdapter
                            }
                        }
                    }
                }
            }
        }
    }
}
