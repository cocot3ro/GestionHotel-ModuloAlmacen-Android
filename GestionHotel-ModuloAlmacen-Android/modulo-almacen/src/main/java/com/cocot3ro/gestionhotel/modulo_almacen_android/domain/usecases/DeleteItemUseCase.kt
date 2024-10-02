package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.RSocketRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val repository: RSocketRepository
) {

    fun invoke(id: Long) {
        repository.delete(id)
            .block()
    }

}