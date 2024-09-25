package com.cocot3ro.gestionhotel.modulo_almacen.mobile.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.network.rsocket.RSocketApiDefinitions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        const val PREFERENCES_NAME = "almacen_preferences"
    }

    private object Keys {
        const val ADDRESS = "address"
        const val PORT = "port"
    }

    object PreferencesKeys {
        val ADDRESS = stringPreferencesKey(Keys.ADDRESS)
        val PORT = intPreferencesKey(Keys.PORT)
    }

    object Defaults {
        const val ADDRESS = RSocketApiDefinitions.DEFAULT_ADDRESS
        const val PORT = RSocketApiDefinitions.DEFAULT_PORT
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun address(address: String) {
        dataStore.edit {
            it[PreferencesKeys.ADDRESS] = address
        }
    }

    fun address(): String {
        var address = Defaults.ADDRESS
        runBlocking {
            dataStore.data.map {
                address = it[PreferencesKeys.ADDRESS] ?: Defaults.ADDRESS
            }.first()
        }
        return address
    }

    suspend fun port(port: Int) {
        dataStore.edit {
            it[PreferencesKeys.PORT] = port
        }
    }

    fun port(): Int {
        var port = Defaults.PORT
        runBlocking {
            dataStore.data.map {
                port = it[PreferencesKeys.PORT] ?: Defaults.PORT
            }.first()
        }

        return port
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun saveSettings(mapSettings: Map<Preferences.Key<*>, *>) {
        dataStore.edit {
            mapSettings.forEach { (k, v) ->
                when (v) {
                    is String -> it[k as Preferences.Key<String>] = v
                    is Int -> it[k as Preferences.Key<Int>] = v
                    is Long -> it[k as Preferences.Key<Long>] = v
                    is Float -> it[k as Preferences.Key<Float>] = v
                    is Double -> it[k as Preferences.Key<Double>] = v
                    is Boolean -> it[k as Preferences.Key<Boolean>] = v
                }
            }
        }
    }

    fun getSettings() =
        dataStore.data.map { preferences ->
            preferences.asMap().toMutableMap().apply {
                putIfAbsent(PreferencesKeys.ADDRESS, Defaults.ADDRESS)
                putIfAbsent(PreferencesKeys.PORT, Defaults.PORT)
            }.toMap()
        }
}