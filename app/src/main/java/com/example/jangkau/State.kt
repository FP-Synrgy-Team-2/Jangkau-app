package com.example.jangkau

sealed interface State<out T> {
    data object Loading : State<Nothing>
    data class Success<out T>(val data: T) : State<T>
    data class Error(val error: String) : State<Nothing>
}