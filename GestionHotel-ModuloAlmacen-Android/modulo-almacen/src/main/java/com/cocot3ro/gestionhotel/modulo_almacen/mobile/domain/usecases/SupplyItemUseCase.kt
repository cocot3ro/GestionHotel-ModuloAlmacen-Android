package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories.RSocketRepository
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.extensions.toModel
import javax.inject.Inject

class SupplyItemUseCase @Inject constructor(
    private val repository: RSocketRepository
) {

    fun invoke(item: AlmacenItem, cantidad: Int) {
        repository.update(item.copy(cantidad = item.cantidad + cantidad).toModel()).block()
    }

}
