package com.example.jangkau

sealed interface State<out T> {
    data object Loading : State<Nothing>
    data class Success<out T>(val data: T) : State<T>
    data class Error(val error: String) : State<Nothing>
}

sealed interface ListState<out T> {
    data object Empty : ListState<Nothing>
    data class Success<out T>(val data: List<T>) : ListState<T>
}

fun <T> List<T>.toListState(): ListState<T> {
    return if (this.isEmpty()) {
        ListState.Empty
    } else {
        ListState.Success(this)
    }
}