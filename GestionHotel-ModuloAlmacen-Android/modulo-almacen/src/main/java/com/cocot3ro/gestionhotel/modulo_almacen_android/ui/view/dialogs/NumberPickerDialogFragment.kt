package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NumberPickerDialogFragment(
    private val minValue: Int,
    private val maxValue: Int,
    private val initialValue: Int,
    private val onNumberSelected: (Int) -> Unit
) : DialogFragment() {

    companion object {
        const val TAG = "NumberPickerDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val numberPicker = NumberPicker(requireContext()).apply {
            this.minValue = this@NumberPickerDialogFragment.minValue
            this.maxValue = this@NumberPickerDialogFragment.maxValue

            this.value = this@NumberPickerDialogFragment.initialValue

            this.wrapSelectorWheel = false
        }

        return AlertDialog.Builder(requireContext())
            .setView(numberPicker)
            .setPositiveButton(getString(R.string.accept)) { _, _ ->
                onNumberSelected.invoke(numberPicker.value)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
    }
}
