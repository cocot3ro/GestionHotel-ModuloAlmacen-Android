package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.extensions

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.entities.NotificationEntity
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem

fun NotificationItem.toDatabase(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        date = date,
        read = read
    )
}