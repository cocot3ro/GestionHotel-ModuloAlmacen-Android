package com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.definitions.NotificationTableDefinition
import com.cocot3ro.gestionhotel.modulo_almacen_android.data.database.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDAO {

    @Query("SELECT * FROM ${NotificationTableDefinition.TABLE_NAME}")
    fun getNotifications(): Flow<List<NotificationEntity>>

    @Insert
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
}