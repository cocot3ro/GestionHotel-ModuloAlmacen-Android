package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.NotificationItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.DeleteNotificationUseCase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.GetNotificationsUseCase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.MarkAsReadUseCase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.SaveNotificationUseCase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            getNotificationsUseCase.invoke()
                .catch {
                    _uiState.value = UiState.Error(it.message ?: "Error desconocido", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun markAsRead(notification: NotificationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            markAsReadUseCase.invoke(notification)
        }
    }

    fun deleteNotification(item: NotificationItem) {
        deleteNotifications(listOf(item))
    }

    fun deleteNotifications(items: List<NotificationItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (item in items) {
                deleteNotificationUseCase.invoke(item)
            }
        }
    }

    fun createNotification(item: NotificationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            saveNotificationUseCase.invoke(item)
        }
    }

}