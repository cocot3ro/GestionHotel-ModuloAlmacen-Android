package com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.definitions.NotificationTableDefinition

@Entity(tableName = NotificationTableDefinition.TABLE_NAME)
data class NotificationEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotificationTableDefinition.COLUMN_ID)
    val id: Long,

    @ColumnInfo(name = NotificationTableDefinition.COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = NotificationTableDefinition.COLUMN_MESSAGE)
    val message: String,

    @ColumnInfo(name = NotificationTableDefinition.COLUMN_DATE)
    val date: Long,

    @ColumnInfo(name = NotificationTableDefinition.COLUMN_READ)
    val read: Boolean
)