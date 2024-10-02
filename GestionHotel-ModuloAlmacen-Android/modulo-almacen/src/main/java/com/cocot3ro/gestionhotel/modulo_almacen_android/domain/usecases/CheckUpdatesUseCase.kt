package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.RestRepository
import javax.inject.Inject

class CheckUpdatesUseCase @Inject constructor(
    private val restRepository: RestRepository
) {

    suspend fun invoke() {
        // TODO: implement this method
        restRepository.getLatestVersion()
    }

}
