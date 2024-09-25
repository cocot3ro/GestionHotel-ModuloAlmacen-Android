package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.R
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.viewholders.TraerItemRvVh

class TraerRvAdapter(
    private val items: List<AlmacenItem>,
    private val showNumberPickerDialog: (min: Int, max: Int, value: Int, item: AlmacenItem, (value: Int) -> Unit) -> Unit
) : RecyclerView.Adapter<TraerItemRvVh>() {

    private val map: MutableMap<AlmacenItem, Int> = mutableMapOf()

    init {
        items.forEach { item ->
            map[item] = 0
        }
    }

    fun getMap(): MutableMap<AlmacenItem, Int> {
        return map
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TraerItemRvVh {
        return TraerItemRvVh(
            LayoutInflater.from(parent.context).inflate(R.layout.traer_almacen_item, parent, false),
            showNumberPickerDialog
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TraerItemRvVh, position: Int) {
        holder.render(map.entries.toList()[position])
    }

}