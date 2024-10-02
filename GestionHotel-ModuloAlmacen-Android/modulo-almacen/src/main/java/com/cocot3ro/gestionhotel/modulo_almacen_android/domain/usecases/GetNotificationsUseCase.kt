package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.AlmacenDbRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val almacenDbRepository: AlmacenDbRepository
) {

    fun invoke() = almacenDbRepository.getNotifications()

}
