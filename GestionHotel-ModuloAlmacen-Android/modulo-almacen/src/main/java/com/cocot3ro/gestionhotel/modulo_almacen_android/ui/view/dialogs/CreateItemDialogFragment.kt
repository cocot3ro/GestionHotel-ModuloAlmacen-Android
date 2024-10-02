package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import com.cocot3ro.gestionhotel.modulo_almacen_android.databinding.FragmentCreateItemDialogBinding
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.AlmacenItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateItemDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentCreateItemDialogBinding

    private lateinit var listener: OnItemCreatedListener

    companion object {
        const val TAG = "CreateItemFragmentDialog"
    }

    interface OnItemCreatedListener {
        fun onItemCreated(item: AlmacenItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = parentFragment as OnItemCreatedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnItemCreatedListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it).apply {
                setView(createView())
                setTitle(getString(R.string.nuevo_elemento))
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
        binding = FragmentCreateItemDialogBinding.inflate(layoutInflater)

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
        dialog.getButton(Dialog.BUTTON_POSITIVE).apply {
            isEnabled = false

            setOnClickListener {
                listener.onItemCreated(
                    AlmacenItem(
                        id = 0L,
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
                    isEnabled = isFormValid()
                }
            )

            binding.editTextCantidad.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isFormValid()
                }
            )

            binding.editTextPack.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isFormValid()
                }
            )

            binding.editTextMinimo.addTextChangedListener(
                afterTextChanged = {
                    isEnabled = isFormValid()
                }
            )
        }
    }

    private fun isFormValid(): Boolean {
        return binding.editTextNombre.text.toString().isNotBlank() &&
                binding.editTextCantidad.text.toString().toIntOrNull() != null &&
                binding.editTextPack.text.toString().toIntOrNull() != null &&
                binding.editTextMinimo.text.toString().toIntOrNull() != null

    }
}
