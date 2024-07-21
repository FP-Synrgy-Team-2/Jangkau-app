package com.example.domain.usecase.user

import com.example.common.Resource
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserUseCase (private val userRepository: UserRepository) {

    operator fun invoke (userId : String) : Flow<Resource<User>> = flow{
        emit(Resource.Loading())
        try {
            val response = userRepository.getUserById(userId)
            emit(Resource.Success(response))
        } catch (e : Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

}