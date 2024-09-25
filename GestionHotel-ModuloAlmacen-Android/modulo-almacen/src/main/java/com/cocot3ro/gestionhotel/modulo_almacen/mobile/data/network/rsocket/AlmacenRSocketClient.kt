package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.network.rsocket

import android.util.Log
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.model.AlmacenModel
import com.google.gson.Gson
import io.rsocket.RSocket
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlmacenRSocketClient @Inject constructor(
    private val gson: Gson
) {

    private lateinit var rSocket: RSocket

    fun all(): Flux<List<AlmacenModel>> {
        return rSocket.requestStream(RSocketUtils.getPayload("/all"))
            .doOnError { Log.e("AlmacenRSocketClient", "Error fetching data", it) }
            .map { gson.fromJson(it.dataUtf8, Array<AlmacenModel>::class.java).toList() }
    }

    fun save(item: AlmacenModel): Mono<AlmacenModel> {
        return rSocket.requestResponse(RSocketUtils.getPayload("/save", gson.toJson(item)))
            .doOnError { Log.e("AlmacenRSocketClient", "Error saving data", it) }
            .map { gson.fromJson(it.dataUtf8, AlmacenModel::class.java) }
    }

    fun update(item: AlmacenModel): Mono<AlmacenModel> {
        return rSocket.requestResponse(RSocketUtils.getPayload("/update", gson.toJson(item)))
            .doOnError { Log.e("AlmacenRSocketClient", "Error updating data", it) }
            .map { gson.fromJson(it.dataUtf8, AlmacenModel::class.java) }
    }

    fun delete(id: Long): Mono<Void> {
        return rSocket.fireAndForget(RSocketUtils.getPayload("/delete", id.toString()))
            .doOnError { Log.e("AlmacenRSocketClient", "Error deleting data", it) }
    }

    fun setRSocket(rSocketConnection: RSocket) {
        rSocket = rSocketConnection
    }

}