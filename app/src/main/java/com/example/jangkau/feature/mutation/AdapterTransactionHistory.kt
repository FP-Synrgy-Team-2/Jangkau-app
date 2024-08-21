package com.example.jangkau.feature.mutation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Transaction
import com.example.jangkau.R
import com.example.jangkau.databinding.ItemTransactionBinding
import com.example.jangkau.formatDate
import com.example.jangkau.moneyFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AdapterTransactionHistory(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<AdapterTransactionHistory.TransactionViewHolder>() {

    class TransactionViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        with(holder.binding) {
            when(transaction.transactionalType){
                "TRANSFER" -> {
                    when(transaction.type){
                        "Pemasukan" -> {
                            amountText.text = "+ ${moneyFormatter(transaction.amount.toLong())}"
                            amountText.setTextColor(holder.itemView.context.getColor(R.color.success))
                            transferInfoText.text = "Dari ${transaction.ownerName} - ${transaction.ownerAccount}"
                        }
                        "Pengeluaran" -> {
                            amountText.text = "- ${moneyFormatter(transaction.amount.toLong())}"
                            transferInfoText.text = "Ke ${transaction.beneficiaryName} - ${transaction.beneficiaryAccount}"
                        }
                    }
                }
                "QRIS" ->{
                    iconTransaction.setImageResource(R.drawable.ic_qris)
                    transferTypeText.text = "QRIS"
                    when(transaction.type){
                        "Pemasukan" -> {
                            amountText.text = "+ ${moneyFormatter(transaction.amount.toLong())}"
                            amountText.setTextColor(holder.itemView.context.getColor(R.color.success))
                            transferInfoText.text = "Dari ${transaction.ownerName}"
                        }
                        "Pengeluaran" -> {
                            amountText.text = "- ${moneyFormatter(transaction.amount.toLong())}"
                            transferInfoText.text = "Ke ${transaction.beneficiaryName}"
                        }
                    }
                }
                else -> {
                    when(transaction.type){
                        "Pemasukan" -> {
                            amountText.text = "+ ${moneyFormatter(transaction.amount.toLong())}"
                            amountText.setTextColor(holder.itemView.context.getColor(R.color.success))
                            transferInfoText.text = "Dari ${transaction.ownerName} - ${transaction.ownerAccount}"
                        }
                        "Pengeluaran" -> {
                            amountText.text = "- ${moneyFormatter(transaction.amount.toLong())}"
                            transferInfoText.text = "Ke ${transaction.beneficiaryName} - ${transaction.beneficiaryAccount}"
                        }
                    }
                }
            }


            timeText.text = formatTime(transaction.date)
        }
    }

    override fun getItemCount() = transactions.size

    private fun formatTime(dateString: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = LocalDateTime.parse(dateString, formatter)
        val timeFormatter = DateTimeFormatter.ofPattern("HH.mm 'WIB'")
        return dateTime.format(timeFormatter)
    }
}
