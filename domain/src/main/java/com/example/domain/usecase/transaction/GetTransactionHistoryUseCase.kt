package com.example.domain.usecase.transaction

import com.example.common.Resource
import com.example.domain.model.Transaction
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class GetTransactionHistoryUseCase (
    private val transactionRepository: TransactionRepository
){
    operator fun invoke(fromDate : String, toDate : String) : Flow<Resource<List<Transaction>>> = flow {
        emit(Resource.Loading())
        try {
            val response = transactionRepository.getTransactionHistory(fromDate,toDate)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

}