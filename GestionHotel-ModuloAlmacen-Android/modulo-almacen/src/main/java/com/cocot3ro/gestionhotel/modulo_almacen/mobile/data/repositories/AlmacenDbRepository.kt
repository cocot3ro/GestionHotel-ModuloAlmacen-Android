package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.repositories

import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.dao.NotificationDAO
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.entities.extensions.toDomain
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.NotificationItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.extensions.toDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlmacenDbRepository @Inject constructor(
    private val notificationDAO: NotificationDAO
) {

    fun getNotifications(): Flow<List<NotificationItem>> {
        return notificationDAO.getNotifications()
            .map { list -> list.map { it.toDomain() } }
    }

    suspend fun insertNotification(notificationItem: NotificationItem): Long {
        return notificationDAO.insertNotification(notificationItem.toDatabase())
    }

    suspend fun updateNotification(notificationItem: NotificationItem) {
        notificationDAO.updateNotification(notificationItem.toDatabase())
    }

    suspend fun deleteNotification(notificationItem: NotificationItem) {
        notificationDAO.deleteNotification(notificationItem.toDatabase())
    }
}