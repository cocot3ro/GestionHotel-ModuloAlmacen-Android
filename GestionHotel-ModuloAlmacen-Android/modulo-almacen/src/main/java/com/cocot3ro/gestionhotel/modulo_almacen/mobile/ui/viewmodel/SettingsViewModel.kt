package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.viewmodel

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.core.DataStoreManager
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases.SaveSettingsUseCase
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
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {

    private val _settingsFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val settingsFlow: StateFlow<UiState> = _settingsFlow

    fun loadSettings() {
        viewModelScope.launch(Dispatchers.Main) {
            dataStoreManager.getSettings()
                .catch {
                    _settingsFlow.value = UiState.Error(it.message ?: "Unknown error", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _settingsFlow.value = UiState.Success(it)
                }
        }
    }

    fun saveSettings(mapSettings: Map<Preferences.Key<*>, *>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSettingsUseCase.invoke(mapSettings)
        }
    }

}