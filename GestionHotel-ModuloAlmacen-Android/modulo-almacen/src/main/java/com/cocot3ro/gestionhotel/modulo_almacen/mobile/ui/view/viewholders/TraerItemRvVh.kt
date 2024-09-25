package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.TraerAlmacenItemBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem

class TraerItemRvVh(
    view: View,
    private val showNumberPickerDialog: (min: Int, max: Int, value: Int, item: AlmacenItem, (value: Int) -> Unit) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = TraerAlmacenItemBinding.bind(view)

    fun render(entry: MutableMap.MutableEntry<AlmacenItem, Int>) {
        val item = entry.key

        binding.tvNombre.text = item.nombre

        if (item.cantidad == 0) {
            binding.tvCantidad.visibility = View.GONE
            binding.numberPicker.visibility = View.GONE
            binding.noStock.visibility = View.VISIBLE
        } else {
            binding.tvCantidad.visibility = View.VISIBLE
            binding.numberPicker.visibility = View.VISIBLE
            binding.noStock.visibility = View.GONE
        }

        binding.numberPicker.setOnClickListener {
            showNumberPickerDialog(
                0,
                item.cantidad,
                binding.tvCantidad.text.toString().toIntOrNull() ?: 0,
                item
            ) { value ->
                entry.setValue(value)
                binding.tvCantidad.text = value.toString()
            }
        }
    }

}
