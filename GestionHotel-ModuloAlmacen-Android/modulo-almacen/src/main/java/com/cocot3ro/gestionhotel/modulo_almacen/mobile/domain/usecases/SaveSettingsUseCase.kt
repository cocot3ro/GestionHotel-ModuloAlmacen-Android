package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import androidx.datastore.preferences.core.Preferences
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.core.DataStoreManager
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    suspend fun invoke(mapSettings: Map<Preferences.Key<*>, *>) {
        dataStoreManager.saveSettings(mapSettings)
    }

}