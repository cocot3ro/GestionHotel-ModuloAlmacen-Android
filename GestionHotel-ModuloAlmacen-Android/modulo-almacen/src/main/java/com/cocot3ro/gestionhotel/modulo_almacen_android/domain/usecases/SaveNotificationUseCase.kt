package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.usecases

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.repositories.AlmacenDbRepository
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val almacenDbRepository: AlmacenDbRepository
) {

    suspend fun invoke(notificationItem: NotificationItem) {
        almacenDbRepository.insertNotification(notificationItem)
    }
}