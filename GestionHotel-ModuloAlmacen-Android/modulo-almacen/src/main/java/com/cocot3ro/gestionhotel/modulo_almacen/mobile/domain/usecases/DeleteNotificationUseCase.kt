package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories.AlmacenDbRepository
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.NotificationItem
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val almacenDbRepository: AlmacenDbRepository
) {

    suspend fun invoke(notificationItem: NotificationItem) {
        almacenDbRepository.deleteNotification(notificationItem)
    }

}