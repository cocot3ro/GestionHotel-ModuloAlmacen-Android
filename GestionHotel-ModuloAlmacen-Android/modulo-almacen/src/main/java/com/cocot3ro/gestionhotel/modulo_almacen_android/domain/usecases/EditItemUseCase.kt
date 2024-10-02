package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.RSocketRepository
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.extensions.toModel
import javax.inject.Inject

class EditItemUseCase @Inject constructor(
    private val repository: RSocketRepository
) {

    fun invoke(item: AlmacenItem) {
        repository.update(item.toModel()).block()
    }

}