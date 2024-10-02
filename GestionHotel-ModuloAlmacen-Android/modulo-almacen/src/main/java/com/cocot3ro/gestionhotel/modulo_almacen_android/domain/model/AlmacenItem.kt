package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model

data class AlmacenItem(
    val id: Long,
    val nombre: String,
    val cantidad: Int,
    val minimo: Int,
    val pack: Int
)