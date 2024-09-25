package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.viewholders

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.R
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.CardItemAlmacenBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem

class AlmacenRvVh(
    view: View,
    private val onTraerListener: (AlmacenItem) -> Unit,
    private val onEditarListener: (AlmacenItem) -> Unit,
    private val onBorrarListener: (AlmacenItem) -> Unit
) : RecyclerView.ViewHolder(view), PopupMenu.OnMenuItemClickListener {

    private val binding: CardItemAlmacenBinding = CardItemAlmacenBinding.bind(view)

    private lateinit var almacenItem: AlmacenItem

    init {
        binding.btnMore.setOnClickListener { showPopupMenu(it) }
    }

    fun render(almacenItem: AlmacenItem) {
        this.almacenItem = almacenItem

        binding.tvNombre.text = almacenItem.nombre
        binding.tvCantidad.text = almacenItem.cantidad.toString()
        binding.tvPack.text = almacenItem.pack.toString()
        binding.tvMinimo.text = almacenItem.minimo.toString()

        setBgColor()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_almacen_item_traer -> {
                onTraerListener.invoke(almacenItem)
                true
            }

            R.id.menu_almacen_item_editar -> {
                onEditarListener.invoke(almacenItem)
                true
            }

            R.id.menu_almacen_item_borrar -> {
                onBorrarListener.invoke(almacenItem)
                true
            }

            else -> false
        }
    }

    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(v.context, v)
        popupMenu.inflate(R.menu.menu_almacen_item)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.show()
    }

    private fun setBgColor() {
        binding.root.apply {
            if (almacenItem.cantidad == 0) {
                setCardBackgroundColor(context.getColor(R.color.almacen_red))
            } else if (almacenItem.cantidad <= almacenItem.minimo) {
                setCardBackgroundColor(context.getColor(R.color.almacen_orange))
            } else {
                setCardBackgroundColor(context.getColor(R.color.white))
            }
        }
    }
}
