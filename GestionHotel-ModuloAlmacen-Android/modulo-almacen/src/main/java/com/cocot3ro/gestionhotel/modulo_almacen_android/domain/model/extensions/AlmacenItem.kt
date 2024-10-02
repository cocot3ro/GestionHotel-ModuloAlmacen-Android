package com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.extensions

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.model.AlmacenModel
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem

fun AlmacenItem.toModel(): AlmacenModel {
    return AlmacenModel(
        id = this.id,
        nombre = this.nombre,
        cantidad = this.cantidad,
        minimo = this.minimo,
        pack = this.pack
    )
}