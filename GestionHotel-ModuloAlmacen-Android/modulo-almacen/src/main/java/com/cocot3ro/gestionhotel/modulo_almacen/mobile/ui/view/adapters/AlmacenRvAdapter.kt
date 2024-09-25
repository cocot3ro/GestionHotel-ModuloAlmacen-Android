package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.R
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.diffutils.AlmacenDiffUtil
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.viewholders.AlmacenRvVh

class AlmacenRvAdapter(
    private var originalList: List<AlmacenItem>,
    private var predicate: ((AlmacenItem) -> Boolean)? = null,
    private val onTraerListener: (AlmacenItem) -> Unit,
    private val onEditarListener: (AlmacenItem) -> Unit,
    private val onBorrarListener: (AlmacenItem) -> Unit
) : RecyclerView.Adapter<AlmacenRvVh>() {

    private var filteredList: List<AlmacenItem> = applyFilter(originalList, predicate)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlmacenRvVh {
        return AlmacenRvVh(
            LayoutInflater.from(parent.context).inflate(R.layout.card_item_almacen, parent, false),
            onTraerListener,
            onEditarListener,
            onBorrarListener
        )
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: AlmacenRvVh, position: Int) {
        holder.render(filteredList[position])
    }

    fun setPredicate(predicate: ((AlmacenItem) -> Boolean)?) {
        this.predicate = predicate
        updateData(originalList)
    }

    fun getPredicate(): ((AlmacenItem) -> Boolean)? {
        return predicate
    }

    fun updateData(newData: List<AlmacenItem>) {
        this.originalList = newData
        val newFilteredList = applyFilter(newData, predicate)
        val diffUtil = AlmacenDiffUtil(filteredList, newFilteredList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        filteredList = newFilteredList
        diffResult.dispatchUpdatesTo(this)
    }

    private fun applyFilter(list: List<AlmacenItem>, predicate: ((AlmacenItem) -> Boolean)?): List<AlmacenItem> {
        return predicate?.let { list.filter(it) } ?: list
    }
}
