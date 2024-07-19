package com.example.domain.usecase.transfer

import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.model.TransferRequest
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TransferRequestUseCase (private val transactionRepository: TransactionRepository) {
    operator fun invoke(transferRequest: TransferRequest) : Flow<Resource<Transaction>> = flow {
        emit(Resource.Loading())
        try {
            val transaction = transactionRepository.makeTransferRequest(transferRequest)
            emit(Resource.Success(transaction))
        } catch (e: Exception) {
            emit(Resource.Error("Error Occurred"))
        }
    }
}