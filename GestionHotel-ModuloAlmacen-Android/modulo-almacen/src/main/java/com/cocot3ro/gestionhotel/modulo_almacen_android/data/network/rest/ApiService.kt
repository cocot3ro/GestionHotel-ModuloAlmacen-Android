package com.cocot3ro.gestionhotel.modulo_almacen_android.data.network.rest

import javax.inject.Inject

class ApiService @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getLatestVersion(): String {
        val response = apiClient.getLatestVersion()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Error getting latest version")
        }
    }

}