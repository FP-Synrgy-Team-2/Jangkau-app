package com.example.domain.usecase.bank_account

import com.example.common.Resource
import com.example.domain.model.BankAccount
import com.example.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShowSavedBankAccUseCase(
    private val bankAccountRepository: BankAccountRepository
) {
    operator fun invoke() : Flow<Resource<List<BankAccount>>> = flow {
        emit(Resource.Loading())
        try {
            val respone = bankAccountRepository.getSavedBankAccount()
            emit(Resource.Success(respone))
        } catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}