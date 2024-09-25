package com.cocot3ro.gestionhotel.modulo_almacen.mobile.data.model

data class AlmacenModel(
    val id: Long,
    val nombre: String,
    val cantidad: Int,
    val minimo: Int,
    val pack: Int
)