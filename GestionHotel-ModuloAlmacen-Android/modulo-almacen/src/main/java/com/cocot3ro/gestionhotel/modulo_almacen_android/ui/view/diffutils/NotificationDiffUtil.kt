package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem

class NotificationDiffUtil(
    private val oldList: List<NotificationItem>,
    private val newList: List<NotificationItem>
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