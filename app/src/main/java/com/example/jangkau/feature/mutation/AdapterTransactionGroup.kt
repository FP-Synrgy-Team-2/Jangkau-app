package com.example.jangkau.feature.mutation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.TransactionGroup
import com.example.jangkau.databinding.ItemDateHeaderBinding

class AdapterTransactionGroup(private val transactionGroups: List<TransactionGroup>) :
    RecyclerView.Adapter<AdapterTransactionGroup.TransactionGroupViewHolder>() {

    class TransactionGroupViewHolder(val binding: ItemDateHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionGroupViewHolder {
        val binding = ItemDateHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionGroupViewHolder, position: Int) {
        val transactionGroup = transactionGroups[position]
        with(holder.binding) {
            dateHeaderText.text = transactionGroup.date

            val transactionAdapter = AdapterTransactionHistory(transactionGroup.transactions)
            transactionHistory.layoutManager = LinearLayoutManager(holder.itemView.context)
            transactionHistory.adapter = transactionAdapter
        }
    }

    override fun getItemCount() = transactionGroups.size
}
