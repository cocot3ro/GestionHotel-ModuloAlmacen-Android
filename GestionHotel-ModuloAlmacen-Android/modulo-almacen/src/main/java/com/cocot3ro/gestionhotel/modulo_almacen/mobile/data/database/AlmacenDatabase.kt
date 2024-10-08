package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.dao.NotificationDAO
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.database.entities.NotificationEntity

@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AlmacenDatabase : RoomDatabase() {

    abstract fun getNotificationDao(): NotificationDAO
}