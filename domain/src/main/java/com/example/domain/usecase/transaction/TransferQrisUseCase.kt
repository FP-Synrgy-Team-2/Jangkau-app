package com.example.domain.usecase.transaction

import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TransferQrisUseCase(private val transactionRepository: TransactionRepository) {
    operator fun invoke(rekeningTujuan : String, nominal : Int) : Flow<Resource<Transaction>> = flow {
        emit(Resource.Loading())
        try{
            val response = transactionRepository.transferQris(rekeningTujuan, nominal)
            emit(Resource.Success(response))
        }catch (e : Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}