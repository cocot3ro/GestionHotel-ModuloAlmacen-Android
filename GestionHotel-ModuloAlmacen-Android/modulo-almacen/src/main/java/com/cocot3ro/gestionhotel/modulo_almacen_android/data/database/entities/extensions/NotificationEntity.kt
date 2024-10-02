package com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.entities.extensions

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.entities.NotificationEntity
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem

fun NotificationEntity.toDomain(): NotificationItem {
    return NotificationItem(
        id = id,
        title = title,
        message = message,
        date = date,
        read = read
    )
}