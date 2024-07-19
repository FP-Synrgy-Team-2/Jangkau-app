package com.example.jangkau.feature.transfer

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.SavedAccount
import com.example.jangkau.base.BaseActivity
import com.example.jangkau.databinding.ActivityTransferBinding
import com.example.jangkau.gone

class TransferActivity : BaseActivity(), AdapterAccountSaved.OnItemClickListener {

    private lateinit var binding: ActivityTransferBinding
    private lateinit var accountAdapter : AdapterAccountSaved
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navbar.imgCancel.gone()

        binding.btnInputBaru.setOnClickListener {
            openTransferInputActivity()
        }

        // Dummy data for demonstration
        val savedAccounts = listOf(
            SavedAccount("Owner 1", "123456789"),
            SavedAccount("Owner 2", "987654321"),
        )

        accountAdapter = AdapterAccountSaved(savedAccounts, this)
        // Set up the RecyclerView
        binding.rvRekeningTersimpan.apply {
            layoutManager = LinearLayoutManager(this@TransferActivity)
            adapter = accountAdapter
        }

    }

    override fun onItemClick(savedAccount: SavedAccount) {
        openTransferInputActivity(savedAccount)
    }
}