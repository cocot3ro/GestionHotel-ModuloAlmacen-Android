package com.cocot3ro.gestionhotel.modulo_almacen_android.data.model.extensions

import com.cocot3ro.gestionhotel.modulo_almacen_android.data.model.AlmacenModel
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem

fun AlmacenModel.toDomain(): AlmacenItem {
    return AlmacenItem(
        id = this.id,
        nombre = this.nombre,
        cantidad = this.cantidad,
        minimo = this.minimo,
        pack = this.pack
    )
}