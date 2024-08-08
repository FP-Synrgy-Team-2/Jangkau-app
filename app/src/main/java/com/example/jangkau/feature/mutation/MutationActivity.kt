package com.example.jangkau.feature.mutation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Transaction
import com.example.domain.model.TransactionGroup
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityMutationBinding
import com.example.jangkau.viewmodel.TransactionViewModel
import org.koin.android.ext.android.inject

class MutationActivity : BaseActivity() {
    private lateinit var binding: ActivityMutationBinding
    private lateinit var transactionAdapter: AdapterTransactionGroup
    private val transactionViewModel: TransactionViewModel by inject()

    private var transactionHistory: List<TransactionGroup> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromDate = intent.getStringExtra("EXTRA_FROM_DATE")
        val toDate = intent.getStringExtra("EXTRA_TO_DATE")

        if (fromDate != null && toDate != null) {
            Log.d("MutationActivity", "From Date: $fromDate, To Date: $toDate")
            transactionViewModel.getTransactionHistory(fromDate, toDate)
            transactionViewModel.transactionsHistory.observe(this) { state ->
                when (state) {
                    is State.Error -> {
                        Log.e("MutationActivity", "Error: ${state.error}")
                        showToast(state.error)
                    }
                    State.Loading -> {
                        Log.d("MutationActivity", "Loading transaction history...")
                    }
                    is State.Success -> {
                        Log.d("MutationActivity", "Transaction history loaded successfully")

                        // Log the data received
                        Log.d("MutationActivity", "Transaction History: ${state.data}")

                        transactionHistory = state.data.map { transactionGroupResponse ->
                            Log.d("MutationActivity", "Mapping TransactionGroup: ${transactionGroupResponse.date}")
                            TransactionGroup(
                                date = transactionGroupResponse.date,
                                transactions = transactionGroupResponse.transactions
                            )
                        }

                        // Initialize the adapter only if it's not already initialized
                        if (!::transactionAdapter.isInitialized) {
                            transactionAdapter = AdapterTransactionGroup(transactionHistory)
                            binding.transactionHistory.layoutManager = LinearLayoutManager(this)
                            binding.transactionHistory.adapter = transactionAdapter
                        } else {
                            transactionAdapter.notifyDataSetChanged()
                        }

                        Log.d("MutationActivity", "Adapter set with ${transactionHistory.size} items")
                    }
                }
            }
        } else {
            Log.e("MutationActivity", "Missing date information in intent")
        }

        binding.btnFilter.setOnClickListener {
            openMutasiFilterActivity()
        }
    }
}
