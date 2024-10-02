package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.gestionhotel.modulo_almacen_android.core.DataStoreManager
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.ConnectToServerUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.GetNotificationsUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import reactor.core.Disposable
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val testConnectionUseCase: ConnectToServerUseCase,
    private val dataStoreManager: DataStoreManager,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val stateFlow: StateFlow<UiState> = _stateFlow

    private val _notificationsStateFlow: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading)
    val notificationsStateFlow: StateFlow<UiState> = _notificationsStateFlow

    private var disposable: Disposable? = null

    fun test() {
        stop()

        disposable = testConnectionUseCase.invoke()
            .doOnError {
                _stateFlow.value = UiState.Error(it.message ?: "Unknown error", it)

            }
            .subscribe {
                _stateFlow.value = UiState.Success(Unit)
            }
    }

    fun getServerUrl(): String {
        return "${dataStoreManager.address()}:${dataStoreManager.port()}"
    }

    fun stop() {
        disposable?.dispose()
        disposable = null
    }

    fun restartFlow() {
        _stateFlow.value = UiState.Loading
    }

    fun fetchNotifications() {
        viewModelScope.launch(Dispatchers.Main) {
            getNotificationsUseCase.invoke()
                .catch {
                    _notificationsStateFlow.value =
                        UiState.Error(it.message ?: "Error desconocido", it)
                }
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    _notificationsStateFlow.value =
                        UiState.Success(list.map { it.read }.count { !it })
                }
        }
    }

}