package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.CreateItemUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.DeleteItemUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.EditItemUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.GetAlmacenItemsUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.GetNotificationsUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.SupplyItemUseCase
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases.TraerItemUseCase
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
class AlmacenViewModel @Inject constructor(
    private val getAlmacenItemsUseCase: GetAlmacenItemsUseCase,
    private val createItemUseCase: CreateItemUseCase,
    private val editItemUseCase: EditItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val traerItemUseCase: TraerItemUseCase,
    private val supplyItemUseCase: SupplyItemUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    private val _almacenItemsStateFlow: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading)
    val almacenItemsStateFlow: StateFlow<UiState> = _almacenItemsStateFlow

    private val _notificationsStateFlow: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading)
    val notificationsStateFlow: StateFlow<UiState> = _notificationsStateFlow

    private var disposable: Disposable? = null

    fun fetchAlmacenItems() {
        viewModelScope.launch(Dispatchers.IO) {
            disposable = getAlmacenItemsUseCase.invoke()
                .subscribe({
                    _almacenItemsStateFlow.value = UiState.Success(it)
                }, {
                    _almacenItemsStateFlow.value = UiState.Error(it.message ?: "Unknown error", it)
                })
        }
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

    fun closeConnection() {
        disposable?.dispose()
        disposable = null
    }

    override fun onCleared() {
        super.onCleared()
        closeConnection()
    }

    fun createItem(item: AlmacenItem) {
        viewModelScope.launch(Dispatchers.IO) {
            createItemUseCase.invoke(item)
        }
    }

    fun editItem(item: AlmacenItem) {
        viewModelScope.launch(Dispatchers.IO) {
            editItemUseCase.invoke(item)
        }
    }

    fun deleteItem(almacenItem: AlmacenItem) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteItemUseCase.invoke(almacenItem.id)
        }
    }

    fun traer(items: Map<AlmacenItem, Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            items.forEach { (item, cantidad) ->
                traerItemUseCase.invoke(item, cantidad)
            }
        }
    }

    fun supply(items: Map<AlmacenItem, Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            items.forEach { (item, cantidad) ->
                supplyItemUseCase.invoke(item, cantidad)
            }
        }
    }
}
