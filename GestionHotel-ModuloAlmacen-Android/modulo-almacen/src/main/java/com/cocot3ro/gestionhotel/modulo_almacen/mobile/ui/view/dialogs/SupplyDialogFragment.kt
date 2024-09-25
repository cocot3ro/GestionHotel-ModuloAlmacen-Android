package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.R
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.FragmentSupplyDialogBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.adapters.SupplyItemRvAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupplyDialogFragment(
    private val items: List<AlmacenItem>
) : DialogFragment() {

    private lateinit var binding: FragmentSupplyDialogBinding

    private lateinit var listener: OnItemsSupplyListener

    private lateinit var supplyItemAdapter: SupplyItemRvAdapter

    companion object {
        const val TAG = "SupplyDialogFragment"
    }

    interface OnItemsSupplyListener {
        fun onItemsSupply(items: Map<AlmacenItem, Int>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnItemsSupplyListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnItemsSupplyListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.add_to_warehouse))
                setPositiveButton(getString(R.string.accept), null)
                setNegativeButton(getString(R.string.cancel), null)
            }.create().apply {
                setOnShowListener {
                    setUpPositiveButton(this)
                }
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun createView(): View {
        binding = FragmentSupplyDialogBinding.inflate(layoutInflater)

        supplyItemAdapter = SupplyItemRvAdapter(items) { _, applyNumber ->
            NumberPickerDialogFragment(0, 1_000_000, 0) {
                applyNumber.invoke(it)
            }.show(childFragmentManager, NumberPickerDialogFragment.TAG)
        }

        binding.recyclerView.apply {
            adapter = supplyItemAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            listener.onItemsSupply(supplyItemAdapter.getMap())
            dialog.dismiss()
        }
    }
}