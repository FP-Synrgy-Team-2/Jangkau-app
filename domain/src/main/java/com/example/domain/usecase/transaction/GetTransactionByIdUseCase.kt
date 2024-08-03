package com.example.domain.usecase.transaction

import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTransactionByIdUseCase (
    private val transactionRepository: TransactionRepository
){
    operator fun invoke(transactionId : String) : Flow<Resource<Transaction>> = flow {
        emit(Resource.Loading())
        try {
            val response = transactionRepository.getTransactionById(transactionId)
            emit(Resource.Success(response))
        }catch (e : Exception){
            emit(Resource.Error(e.message.toString()))
        }

    }
}