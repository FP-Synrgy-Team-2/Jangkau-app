package com.example.jangkau.feature.mutation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.model.Transaction
import com.example.domain.model.TransactionGroup
import com.example.jangkau.R
import com.example.jangkau.State
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityMutationBinding
import com.example.jangkau.viewmodel.TransactionViewModel
import org.koin.android.ext.android.inject

class MutationActivity : BaseActivity() {
    private lateinit var binding: ActivityMutationBinding
    private lateinit var transactionAdapter: AdapterTransactionGroup
    private val transactionViewModel : TransactionViewModel by inject()

    private var transactionHistory : List<TransactionGroup> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromDate = intent.getStringExtra("EXTRA_FROM_DATE")
        val toDate = intent.getStringExtra("EXTRA_TO_DATE")

        if (fromDate != null && toDate != null) {
            transactionViewModel.getTransactionHistory(fromDate, toDate)
            transactionViewModel.transactionsHistory.observe(this){state->
                when(state){
                    is State.Error -> {
                        showToast(state.error)
                    }
                    State.Loading -> {
                        // Handle loading state if necessary
                    }
                    is State.Success -> {
                        transactionHistory = state.data
                        transactionAdapter = AdapterTransactionGroup(transactionHistory)
                        binding.transactionHistory.adapter = transactionAdapter
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