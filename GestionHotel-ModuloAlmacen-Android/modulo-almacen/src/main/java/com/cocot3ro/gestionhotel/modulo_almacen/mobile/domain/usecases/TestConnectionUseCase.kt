package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories.RSocketRepository
import javax.inject.Inject

class TestConnectionUseCase @Inject constructor(
    private val rSocketRepository: RSocketRepository
) {

    fun invoke() = rSocketRepository.status()

}