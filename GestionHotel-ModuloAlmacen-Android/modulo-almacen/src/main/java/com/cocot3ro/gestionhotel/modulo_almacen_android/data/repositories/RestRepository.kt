package com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.network.rest.ApiService
import javax.inject.Inject

class RestRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getLatestVersion(): String {
        return apiService.getLatestVersion()
    }

}