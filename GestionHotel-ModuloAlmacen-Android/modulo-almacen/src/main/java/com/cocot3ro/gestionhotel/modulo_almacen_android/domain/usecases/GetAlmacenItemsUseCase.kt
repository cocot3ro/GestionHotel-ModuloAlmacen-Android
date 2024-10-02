package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.model.extensions.toDomain
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.RSocketRepository
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem
import reactor.core.publisher.Flux
import javax.inject.Inject

class GetAlmacenItemsUseCase @Inject constructor(
    private var rSocketRepository: RSocketRepository
) {

    fun invoke(): Flux<List<AlmacenItem>> {
        return rSocketRepository.all().map { list -> list.map { it.toDomain() } }
    }
}
