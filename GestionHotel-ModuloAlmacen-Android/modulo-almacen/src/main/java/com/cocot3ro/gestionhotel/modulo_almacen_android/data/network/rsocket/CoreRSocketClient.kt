package com.cocot3ro.gestionhotel.modulo_almacen_android.data.network.rsocket

import android.util.Log
import io.rsocket.RSocket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoreRSocketClient @Inject constructor() {

    private lateinit var rSocket: RSocket

    fun status(): Boolean {
        val response = rSocket.requestResponse(RSocketUtils.getPayload("/status"))
            .map { it.dataUtf8 }
            .doOnError { Log.e("CoreRSocketClient", "Error pinging server", it) }
            .block()

        return !response.isNullOrBlank()
    }

    fun setRSocket(rSocketConnection: RSocket) {
        rSocket = rSocketConnection
    }
}