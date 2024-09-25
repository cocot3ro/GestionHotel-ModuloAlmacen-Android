package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.network.rest.ApiService
import javax.inject.Inject

class RestRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getLatestVersion(): String {
        return apiService.getLatestVersion()
    }

}