package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.RSocketRepository
import javax.inject.Inject

class CloseConnectionUseCase @Inject constructor(
    private val rSocketRepository: RSocketRepository
) {

    fun invoke() = rSocketRepository.close()

}