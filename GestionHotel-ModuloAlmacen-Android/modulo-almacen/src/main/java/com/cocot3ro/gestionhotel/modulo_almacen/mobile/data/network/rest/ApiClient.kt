package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.network.rest

import retrofit2.Response
import retrofit2.http.GET

interface ApiClient {

    @GET("version")
    suspend fun getLatestVersion(): Response<String>

}