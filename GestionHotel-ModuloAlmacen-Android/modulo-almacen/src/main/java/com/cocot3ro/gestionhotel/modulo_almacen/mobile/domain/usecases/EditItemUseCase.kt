package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories.RSocketRepository
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.extensions.toModel
import javax.inject.Inject

class EditItemUseCase @Inject constructor(
    private val repository: RSocketRepository
) {

    fun invoke(item: AlmacenItem) {
        repository.update(item.toModel()).block()
    }

}