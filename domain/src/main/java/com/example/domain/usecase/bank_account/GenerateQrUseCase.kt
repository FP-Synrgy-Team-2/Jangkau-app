package com.example.domain.usecase.bank_account

import android.graphics.Bitmap
import com.example.common.Resource
import com.example.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GenerateQrUseCase (
    private val bankAccountRepository: BankAccountRepository
) {
    operator fun invoke() : Flow<Resource<Bitmap>> = flow {
        emit(Resource.Loading())
        try {
            val response = bankAccountRepository.generateQr()
            emit(Resource.Success(response))
        } catch (e : Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}