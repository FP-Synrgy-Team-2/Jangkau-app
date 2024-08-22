package com.example.domain.usecase.bank_account

import com.example.common.Resource
import com.example.domain.model.BankAccount
import com.example.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchDataBankByScanQr(
    private val bankAccountRepository: BankAccountRepository
) {
    operator fun invoke (encryptedData : String) : Flow<Resource<BankAccount>> = flow{
        emit(Resource.Loading())
        try {
            val response = bankAccountRepository.scanQr(encryptedData)
            emit(Resource.Success(response))
        }catch (e : Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}