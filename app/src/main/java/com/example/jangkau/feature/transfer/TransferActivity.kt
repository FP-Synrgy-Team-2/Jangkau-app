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
import org.koin.android.ext.android.inject

class TransferActivity : BaseActivity(), AdapterAccountSaved.OnItemClickListener {

    private lateinit var binding: ActivityTransferBinding
    private lateinit var accountAdapter : AdapterAccountSaved
    private val bankViewModel : BankAccountViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObserver()

        binding.navbar.imgCancel.gone()
        binding.navbar.imgBackArrow.setOnClickListener {
            finish()
        }

        binding.btnInputBaru.setOnClickListener {
            openTransferInputActivity()
        }



    }

    override fun onItemClick(savedAccount: BankAccount) {
        openTransferInputActivity(savedAccount)
    }

    private fun setObserver() {

        bankViewModel.showSavedBankAcc()
        bankViewModel.savedBankAcc.observe(this) {state->
            when(state){
                is State.Error -> {
                    showToast(state.error)
                }
                State.Loading -> {

                }
                is State.Success -> {
                    when (val data = state.data) {

                        ListState.Empty -> {
                            binding.tvRekeningTersimpan.text = "Belum ada Rekening yang Tersimpan"
                        }
                        is ListState.Success -> {

                            accountAdapter = AdapterAccountSaved(data.data.toMutableList(), this)

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