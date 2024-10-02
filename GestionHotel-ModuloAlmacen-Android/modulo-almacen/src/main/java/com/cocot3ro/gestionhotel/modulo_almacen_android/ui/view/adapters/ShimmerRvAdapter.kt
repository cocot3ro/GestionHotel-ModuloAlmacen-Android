package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.viewholders.ShimmerRvVh

class ShimmerRvAdapter : RecyclerView.Adapter<ShimmerRvVh>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShimmerRvVh {
        return ShimmerRvVh(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item_almacen_unload, parent, false)
        )
    }

    override fun getItemCount(): Int = 99

    override fun onBindViewHolder(holder: ShimmerRvVh, position: Int) {}

}