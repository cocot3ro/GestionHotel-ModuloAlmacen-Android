package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.RSocketRepository
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.extensions.toModel
import javax.inject.Inject

class TraerItemUseCase @Inject constructor(
    private val repository: RSocketRepository
) {

    fun invoke(item: AlmacenItem, cantidad: Int) {
        repository.update(item.copy(cantidad = item.cantidad - cantidad).toModel()).block()
    }

}