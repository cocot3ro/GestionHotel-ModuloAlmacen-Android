package com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model

data class NotificationItem(
    val id: Long,
    var title: String,
    var message: String,
    var date: Long,
    var read: Boolean
)