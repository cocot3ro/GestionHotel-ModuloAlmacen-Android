package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem

class AlmacenDiffUtil(
    private val oldList: List<AlmacenItem>,
    private val newList: List<AlmacenItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}