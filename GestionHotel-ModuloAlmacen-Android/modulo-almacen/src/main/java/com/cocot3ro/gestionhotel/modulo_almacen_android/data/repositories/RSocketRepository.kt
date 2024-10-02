package com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories

import android.util.Log
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.model.AlmacenModel
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.network.rsocket.AlmacenRSocketClient
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.network.rsocket.CoreRSocketClient
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import reactor.core.publisher.Mono
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RSocketRepository @Inject constructor(
    private val rSocketConnector: RSocketConnector,
    private val almacenRSocketClient: AlmacenRSocketClient,
    private val coreRSocketClient: CoreRSocketClient
) {

    private var rSocketConnection: RSocket? = null

    fun connect(address: String, port: Int): Mono<RSocket> {
        close()

        return rSocketConnector.connect(TcpClientTransport.create(address, port))
            .doOnSuccess { connection ->
                rSocketConnection = connection
                almacenRSocketClient.setRSocket(rSocketConnection!!)
                coreRSocketClient.setRSocket(rSocketConnection!!)
            }
            .doOnError {
                Log.e("RSocketRepository", "Error connecting to server", it)
            }
    }

    fun status() = coreRSocketClient.status()

    fun all() = almacenRSocketClient.all()

    fun save(item: AlmacenModel) = almacenRSocketClient.save(item)

    fun update(item: AlmacenModel) = almacenRSocketClient.update(item)

    fun delete(id: Long) = almacenRSocketClient.delete(id)

    fun close() {
        rSocketConnection?.dispose()
    }

}