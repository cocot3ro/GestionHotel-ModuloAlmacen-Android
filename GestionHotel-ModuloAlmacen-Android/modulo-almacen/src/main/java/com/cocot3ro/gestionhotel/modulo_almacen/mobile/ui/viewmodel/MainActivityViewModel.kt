package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.CheckUpdatesUseCase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.CloseConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val closeConnectionUseCase: CloseConnectionUseCase,
    private val checkUpdatesUseCase: CheckUpdatesUseCase

) : ViewModel() {

    fun disconnect() {
        closeConnectionUseCase.invoke()
    }

    fun checkUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            checkUpdatesUseCase.invoke()
        }
    }

}
