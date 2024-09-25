package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories.AlmacenDbRepository
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.NotificationItem
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val almacenDbRepository: AlmacenDbRepository
) {

    suspend fun invoke(notificationItem: NotificationItem) {
        notificationItem.read = true
        almacenDbRepository.updateNotification(notificationItem)
    }

}
