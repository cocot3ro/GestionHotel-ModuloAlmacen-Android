package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen_android.databinding.CardItemNotificationBinding
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem

class NotificationRvVh(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CardItemNotificationBinding.bind(view)

    fun render(notificationItem: NotificationItem) {
        binding.tvTitle.text = notificationItem.title
    }

}
