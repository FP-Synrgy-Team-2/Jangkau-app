package com.example.domain.usecase.transaction

import android.util.Log
import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TransferRequestUseCase (private val transactionRepository: TransactionRepository) {
    operator fun invoke(rekeningTujuan: String, nominal: Int, catatan: String, isSaved : Boolean) : Flow<Resource<Transaction>> = flow {
        emit(Resource.Loading())
        try {
            val response = transactionRepository.makeTransferRequest(
                rekeningTujuan = rekeningTujuan,
                nominal = nominal,
                catatan = catatan,
                isSaved = isSaved
            )
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            Log.e("TransferRequestUseCase", "Error making transfer request", e)
        }

    }
}