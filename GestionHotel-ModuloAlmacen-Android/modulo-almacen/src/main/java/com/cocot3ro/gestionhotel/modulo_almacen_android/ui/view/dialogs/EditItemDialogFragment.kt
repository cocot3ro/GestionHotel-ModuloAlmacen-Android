package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import com.cocot3ro.gestionhotel.modulo_almacen_android.databinding.FragmentEditItemDialogBinding
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditItemDialogFragment(private val item: AlmacenItem) : DialogFragment() {

    private lateinit var binding: FragmentEditItemDialogBinding

    private lateinit var listener: EditItemFragmentDialogListener

    companion object {
        const val TAG = "EditItemFragmentDialog"

        fun newInstance(item: AlmacenItem): EditItemDialogFragment {
            return EditItemDialogFragment(item)
        }
    }

    interface EditItemFragmentDialogListener {
        fun onItemEdited(item: AlmacenItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as EditItemFragmentDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement EditItemFragmentDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.editar_elemento))
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
        binding = FragmentEditItemDialogBinding.inflate(layoutInflater)

        binding.editTextNombre.setText(item.nombre)
        binding.editTextCantidad.setText(item.cantidad.toString())
        binding.editTextMinimo.setText(item.minimo.toString())
        binding.editTextPack.setText(item.pack.toString())

        binding.editTextNombre.apply {
            addTextChangedListener(
                afterTextChanged = {
                    error = if (text.toString().isBlank()) {
                        context.getString(R.string.field_cannot_be_empty)
                    } else {
                        null
                    }
                }
            )
        }

        binding.editTextCantidad.apply {
            addTextChangedListener(
                afterTextChanged = {
                    error = if (text.toString().isBlank()) {
                        context.getString(R.string.field_cannot_be_empty)
                    } else if (text.toString().toIntOrNull() == null || text.toString()
                            .toInt() < 0
                    ) {
                        context.getString(R.string.invalid_number)
                    } else {
                        null
                    }
                }
            )
        }

        binding.editTextPack.apply {
            addTextChangedListener(
                afterTextChanged = {
                    error = if (text.toString().isBlank()) {
                        context.getString(R.string.field_cannot_be_empty)
                    } else if (text.toString().toIntOrNull() == null || text.toString()
                            .toInt() < 0
                    ) {
                        context.getString(R.string.invalid_number)
                    } else {
                        null
                    }
                }
            )
        }

        binding.editTextMinimo.apply {
            addTextChangedListener(
                afterTextChanged = {
                    error = if (text.toString().isBlank()) {
                        context.getString(R.string.field_cannot_be_empty)
                    } else if (text.toString().toIntOrNull() == null) {
                        context.getString(R.string.invalid_number)
                    } else {
                        null
                    }
                }
            )
        }

        return binding.root
    }

    private fun setUpPositiveButton(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            isEnabled = false

            setOnClickListener {
                listener.onItemEdited(
                    AlmacenItem(
                        id = item.id,
                        nombre = binding.editTextNombre.text.toString(),
                        cantidad = binding.editTextCantidad.text.toString().toInt(),
                        minimo = binding.editTextMinimo.text.toString().toInt(),
                        pack = binding.editTextPack.text.toString().toInt()
                    )
                )

                dialog.dismiss()
            }

            binding.editTextNombre.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isValidForm()
                }
            )

            binding.editTextCantidad.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isValidForm()
                }
            )

            binding.editTextPack.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isValidForm()
                }
            )

            binding.editTextMinimo.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isValidForm()
                }
            )
        }
    }

    private fun isValidForm(): Boolean {
        return binding.editTextNombre.text.toString() != item.nombre ||
                (binding.editTextCantidad.text.toString().toIntOrNull() ?: item.cantidad) != item.cantidad ||
                (binding.editTextPack.text.toString().toIntOrNull() ?: item.pack) != item.pack ||
                (binding.editTextMinimo.text.toString().toIntOrNull() ?: item.minimo) != item.minimo
    }
}