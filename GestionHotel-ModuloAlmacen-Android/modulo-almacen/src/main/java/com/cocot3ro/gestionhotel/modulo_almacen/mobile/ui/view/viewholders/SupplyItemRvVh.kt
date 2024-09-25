package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.SupplyAlmacenItemBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem

class SupplyItemRvVh(
    view: View,
    private val showNumberPickerDialog: (item: AlmacenItem, (value: Int) -> Unit) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = SupplyAlmacenItemBinding.bind(view)

    fun render(entry: MutableMap.MutableEntry<AlmacenItem, Int>) {
        val item = entry.key

        binding.tvNombre.text = item.nombre

        binding.numberPicker.setOnClickListener {
            showNumberPickerDialog(item) { value ->
                entry.setValue(value)
                binding.tvCantidad.text = value.toString()
            }
        }
    }
}
