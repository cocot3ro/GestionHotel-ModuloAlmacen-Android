package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.R
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.FragmentStartScreenBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.states.UiState
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.viewmodel.StartScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartScreenFragment : Fragment() {

    private lateinit var binding: FragmentStartScreenBinding

    private val viewModel: StartScreenViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var menuProvider: MenuProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_start_screen_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_start_screen_notifications -> {
                        navController.navigate(R.id.notificationsFragment)
                        true
                    }

                    else -> false
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartScreenBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.tvServer.text = viewModel.getServerUrl()

        binding.btnTryAgain.setOnClickListener {
            clear()
            checkConnection()
        }

        binding.fabOptions.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MenuHost).addMenuProvider(
            menuProvider,
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            clear()
                        }

                        is UiState.Success<*> -> {
                            navController.navigate(R.id.almacenFragment)
                        }

                        is UiState.Error -> {
                            error()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationsStateFlow.collect { state ->
                    when (state) {
                        is UiState.Loading -> {

                        }

                        is UiState.Success<*> -> {
                            if (state.data as Int > 0) {
                                binding.toolbar.menu.findItem(R.id.menu_start_screen_notifications)
                                    .setIcon(R.drawable.notifications_unread_32dp)
                            } else {
                                binding.toolbar.menu.findItem(R.id.menu_start_screen_notifications)
                                    .setIcon(R.drawable.notifications_32dp)
                            }
                        }

                        is UiState.Error -> {

                        }
                    }
                }
            }
        }

        viewModel.restartFlow()

        checkConnection()
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchNotifications()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stop()
    }

    private fun clear() {
        binding.tvFailedToConnect.visibility = View.GONE
        binding.tvTryingToConnect.visibility = View.VISIBLE
        binding.btnTryAgain.visibility = View.GONE
    }

    private fun error() {
        binding.tvFailedToConnect.visibility = View.VISIBLE
        binding.tvTryingToConnect.visibility = View.GONE
        binding.btnTryAgain.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun checkConnection() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.test()
        }
    }
}
