package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.diffutils.NotificationDiffUtil
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.viewholders.NotificationRvVh

class NotificationsRvAdapter(
    private var list: List<NotificationItem>
) : RecyclerView.Adapter<NotificationRvVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationRvVh {
        return NotificationRvVh(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item_notification, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NotificationRvVh, position: Int) {
        holder.render(list[position])
    }

    fun updateList(newList: List<NotificationItem>) {
        val diffUtil = NotificationDiffUtil(list, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newList
        result.dispatchUpdatesTo(this)
    }

    fun getData(): List<NotificationItem> {
        return list
    }

}
