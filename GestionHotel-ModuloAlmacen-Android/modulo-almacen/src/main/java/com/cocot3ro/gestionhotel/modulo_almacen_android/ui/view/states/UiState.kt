package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.states

sealed class UiState {
    data object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error(val errorMessage: String, val exception: Throwable) : UiState()
}