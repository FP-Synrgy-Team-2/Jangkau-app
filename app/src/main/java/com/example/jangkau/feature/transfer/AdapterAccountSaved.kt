package com.example.jangkau.feature.transfer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.BankAccount
import com.example.domain.model.SavedAccount
import com.example.jangkau.databinding.ItemRekeningTersimpanBinding

class AdapterAccountSaved(private val savedAccountList: List<BankAccount>?, private val listener: OnItemClickListener) : RecyclerView.Adapter<AdapterAccountSaved.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRekeningTersimpanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return savedAccountList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = savedAccountList?.get(position)
        if (account != null) {
            holder.bind(account)
            holder.itemView.setOnClickListener {
                listener.onItemClick(account)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemRekeningTersimpanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: BankAccount) {
            binding.apply {
                tvNamaPemilik.text = account.ownerName
                tvNomorRekening.text = account.accountNumber
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(savedAccount: BankAccount)
    }



}