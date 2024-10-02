package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import com.cocot3ro.gestionhotel.modulo_almacen_android.core.DataStoreManager.PreferencesKeys
import com.cocot3ro.gestionhotel.modulo_almacen_android.databinding.FragmentSettingsBinding
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.states.UiState
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var originalAddress: String
    private var originalPort: Int = 0

    private var address: String? = null
    private var port: Int? = null

    private var addressChanged = false
    private var portChanged = false

    private val ipAddressRegex = """^((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.?\b){4}$|^(localhost)$""".toRegex()
    private val portRegex =
        """^(6553[0-5]|655[0-2]\d|65[0-4]\d{2}|6[0-4]\d{3}|[1-5]\d{4}|\d{1,4})$""".toRegex()

    private object BundleKeys {
        const val ORIGINAL_ADDRESS_KEY = "original_address_key"
        const val ADDRESS_KEY = "address_key"
        const val ORIGINAL_PORT_KEY = "original_port_key"
        const val PORT_KEY = "port_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (addressChanged || portChanged) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.discard))
                        .setMessage(getString(R.string.discard_changes))
                        .setPositiveButton(getString(R.string.discard)) { _, _ ->
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.setNavigationOnClickListener {
            if (addressChanged || portChanged) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.discard))
                    .setMessage(getString(R.string.discard_changes))
                    .setPositiveButton(getString(R.string.discard)) { _, _ ->
                        navController.popBackStack()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            } else {
                navController.popBackStack()
            }
        }

        binding.fabSave.setOnClickListener {
            if (!validIpAddress() || !validPort()) return@setOnClickListener

            viewModel.saveSettings(
                mapOf(
                    Pair(PreferencesKeys.ADDRESS, binding.etAddress.text.toString()),
                    Pair(PreferencesKeys.PORT, binding.etPort.text.toString().toInt())
                )
            )

            addressChanged = false
            portChanged = false
            updateFab()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingsFlow.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {}

                        is UiState.Success<*> -> {
                            val data = (uiState.data as MutableMap<*, *>)
                                .map { (k, v): Map.Entry<*, *> -> k as Preferences.Key<*> to v!! }
                                .toMap()

                            originalAddress = data[PreferencesKeys.ADDRESS] as String
                            originalPort = data[PreferencesKeys.PORT] as Int

                            address = originalAddress
                            port = originalPort

                            savedInstanceState?.run {
                                address = getString(BundleKeys.ADDRESS_KEY)
                                port = getInt(BundleKeys.PORT_KEY)
                            }

                            binding.etAddress.setText(address)
                            binding.etPort.setText(port.toString())
                        }

                        is UiState.Error -> {
                            Log.e(
                                "SettingsFragment",
                                "Error: ${uiState.errorMessage}",
                                uiState.exception
                            )
                        }
                    }
                }
            }
        }

        viewModel.loadSettings()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.run {
            originalAddress = getString(BundleKeys.ORIGINAL_ADDRESS_KEY)!!
            originalPort = getInt(BundleKeys.ORIGINAL_PORT_KEY)

            val address =
                getString(
                    BundleKeys.ADDRESS_KEY,
                    this@SettingsFragment.address ?: originalAddress
                )
            val port = getInt(BundleKeys.PORT_KEY, this@SettingsFragment.port ?: originalPort)

            binding.etAddress.setText(address)
            binding.etPort.setText(port.toString())
        }

        updateFab()
    }

    override fun onStart() {
        super.onStart()

        binding.etAddress.apply {
            addTextChangedListener(
                afterTextChanged = {
                    addressChanged = it.toString() != originalAddress

                    error = if (text.toString().isBlank()) {
                        context.getString(R.string.field_cannot_be_empty)
                    } else if (!validIpAddress()) {
                        context.getString(R.string.invalid_ip_address)
                    } else {
                        null
                    }

                    updateFab()
                }
            )
        }

        binding.etPort.apply {
            addTextChangedListener(
                afterTextChanged = { str ->
                    portChanged = str.toString().takeIf { it.isNotBlank() }?.toInt() != originalPort

                    error = if (text.toString().isBlank()) {
                        context.getString(R.string.field_cannot_be_empty)
                    } else if (!validPort()) {
                        context.getString(R.string.invalid_port)
                    } else {
                        null
                    }

                    updateFab()
                }
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(BundleKeys.ORIGINAL_ADDRESS_KEY, originalAddress)
        outState.putString(BundleKeys.ADDRESS_KEY, binding.etAddress.text.toString())

        outState.putInt(BundleKeys.ORIGINAL_PORT_KEY, originalPort)
        outState.putInt(BundleKeys.PORT_KEY, binding.etPort.text.toString().toInt())
    }

    private fun updateFab() {
        if (addressChanged || portChanged) binding.fabSave.show()
        else binding.fabSave.hide()
    }

    private fun validIpAddress(): Boolean {
        return ipAddressRegex.matches(binding.etAddress.text.toString())
    }

    private fun validPort(): Boolean {
        return portRegex.matches(binding.etPort.text.toString())
    }
}
