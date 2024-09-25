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
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.FragmentTraerDialogBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.adapters.TraerRvAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TraerDialogFragment(
    private val items: List<AlmacenItem>
) : DialogFragment() {

    private lateinit var binding: FragmentTraerDialogBinding

    private lateinit var listener: OnItemsTraerListener

    private lateinit var traerItemAdapter: TraerRvAdapter

    companion object {
        const val TAG = "TraerSingleItemFragmentDialog"
    }

    interface OnItemsTraerListener {
        fun onItemsTraer(items: Map<AlmacenItem, Int>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnItemsTraerListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnItemTraerListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.traer_del_almacen))
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
        binding = FragmentTraerDialogBinding.inflate(layoutInflater)

        traerItemAdapter = TraerRvAdapter(items) { min, max, value, _, applyNumber ->
            NumberPickerDialogFragment(min, max, value) {
                applyNumber(it)
            }.show(childFragmentManager, NumberPickerDialogFragment.TAG)
        }

        binding.recyclerView.apply {
            adapter = traerItemAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            listener.onItemsTraer(traerItemAdapter.getMap())

            dialog.dismiss()
        }
    }
}