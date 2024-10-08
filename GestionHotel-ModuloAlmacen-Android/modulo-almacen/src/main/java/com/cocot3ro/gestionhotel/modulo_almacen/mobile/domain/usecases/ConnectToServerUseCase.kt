package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.core.DataStoreManager
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories.RSocketRepository
import io.rsocket.RSocket
import reactor.core.publisher.Mono
import javax.inject.Inject

class ConnectToServerUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val rSocketRepository: RSocketRepository
) {

    fun invoke(): Mono<RSocket> {
        return rSocketRepository.connect(dataStoreManager.address(), dataStoreManager.port())
    }

}