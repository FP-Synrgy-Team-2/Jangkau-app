package com.example.domain.usecase.bank_account

import com.example.common.Resource
import com.example.domain.model.BankAccount
import com.example.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
class ShowDataBankAccUseCase(private val bankAccountRepository : BankAccountRepository) {
    operator fun invoke() : Flow<Resource<BankAccount>> = flow {
        emit(Resource.Loading())
        try {
            val response = bankAccountRepository.getBankAccountById()
        } catch (e:Exception){
            emit(Resource.Error("Error Occurred"))
        }
    }

}