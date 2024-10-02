package com.cocot3ro.gestionhotel.modulo_almacen_android.data.network.rest

import retrofit2.Response
import retrofit2.http.GET

interface ApiClient {

    @GET("version")
    suspend fun getLatestVersion(): Response<String>

}