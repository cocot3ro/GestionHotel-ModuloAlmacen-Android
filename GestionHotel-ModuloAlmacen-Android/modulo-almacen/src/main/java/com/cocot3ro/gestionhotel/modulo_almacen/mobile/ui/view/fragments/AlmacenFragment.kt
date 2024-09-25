package com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.fragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.R
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.databinding.FragmentAlmacenBinding
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.domain.model.AlmacenItem
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.adapters.AlmacenRvAdapter
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.adapters.ShimmerRvAdapter
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.dialogs.CreateItemDialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.dialogs.EditItemDialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.dialogs.SupplyDialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.dialogs.TraerDialogFragment
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.view.states.UiState
import com.cocot3ro.gestionhotel.modulo_almacen.mobile.ui.viewmodel.AlmacenViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlmacenFragment : Fragment(),
    CreateItemDialogFragment.OnItemCreatedListener,
    EditItemDialogFragment.EditItemFragmentDialogListener,
    TraerDialogFragment.OnItemsTraerListener,
    SupplyDialogFragment.OnItemsSupplyListener {

    private lateinit var binding: FragmentAlmacenBinding

    private val viewModel: AlmacenViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var almacenAdapter: AlmacenRvAdapter

    private lateinit var menuProvider: MenuProvider

    private var data: List<AlmacenItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_almacen_fragment, menu)

                MenuCompat.setGroupDividerEnabled(menu, true)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.menu_almacen_notifications -> {
                        navController.navigate(R.id.notificationsFragment)
                        true
                    }

                    R.id.menu_almacen_filter -> {
                        if (almacenAdapter.getPredicate() == null) {
                            almacenAdapter.setPredicate { it.cantidad <= it.minimo }
                            menuItem.setIcon(R.drawable.filter_alt_off_32dp)
                        } else {
                            almacenAdapter.setPredicate(null)
                            menuItem.setIcon(R.drawable.filter_alt_32dp)
                        }

                        true
                    }

                    R.id.menu_almacen_nuevo -> {
                        CreateItemDialogFragment().show(
                            childFragmentManager,
                            CreateItemDialogFragment.TAG
                        )
                        true
                    }

                    R.id.menu_almacen_traer -> {
                        TraerDialogFragment(data).show(
                            childFragmentManager,
                            TraerDialogFragment.TAG
                        )
                        true
                    }

                    R.id.menu_almacen_supply -> {
                        SupplyDialogFragment(data).show(
                            childFragmentManager,
                            SupplyDialogFragment.TAG
                        )
                        true
                    }

                    R.id.menu_almacen_settings -> {
                        navController.popBackStack()
                        navController.navigate(R.id.settingsFragment)
                        true
                    }

                    else -> false
                }
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        almacenAdapter = AlmacenRvAdapter(
            originalList = emptyList(),
            onTraerListener = { almacenItem ->
                TraerDialogFragment(listOf(almacenItem)).show(
                    childFragmentManager,
                    TraerDialogFragment.TAG
                )
            },
            onEditarListener = { almacenItem ->
                EditItemDialogFragment.newInstance(almacenItem).show(
                    childFragmentManager,
                    EditItemDialogFragment.TAG
                )
            },
            onBorrarListener = { almacenItem ->
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.eliminar))
                    .setMessage(getString(R.string.seguro_de_eliminar, almacenItem.nombre))
                    .setPositiveButton(getString(R.string.accept)) { _, _ ->
                        viewModel.deleteItem(almacenItem)
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlmacenBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(setOf(R.id.almacenFragment))
        )

        binding.recyclerView.apply {
            adapter = almacenAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                justifyContent = JustifyContent.CENTER
                alignItems = AlignItems.CENTER
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
        }

        binding.shimmerRecyclerView.apply {
            adapter = ShimmerRvAdapter()
            layoutManager = FlexboxLayoutManager(context).apply {
                justifyContent = JustifyContent.CENTER
                alignItems = AlignItems.CENTER
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
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
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.almacenItemsStateFlow.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.errorView.root.visibility = View.GONE
                            binding.shimmerViewContainer.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }

                        is UiState.Success<*> -> {
                            binding.errorView.root.visibility = View.GONE
                            binding.shimmerViewContainer.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE

                            data = (uiState.data as List<*>).map { it as AlmacenItem }

                            almacenAdapter.updateData(data)

                            if (data.isEmpty()) {
                                binding.tvEmpty.visibility = View.VISIBLE
                            } else {
                                binding.tvEmpty.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            binding.errorView.apply {
                                root.visibility = View.VISIBLE
                                btnRetry.setOnClickListener { navController.popBackStack() }
                            }

                            closeDialogs()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.notificationsStateFlow.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {

                        }

                        is UiState.Success<*> -> {
                            if (uiState.data as Int > 0) {
                                binding.toolbar.menu.findItem(R.id.menu_almacen_notifications)
                                    .setIcon(R.drawable.notifications_unread_32dp)
                            } else {
                                binding.toolbar.menu.findItem(R.id.menu_almacen_notifications)
                                    .setIcon(R.drawable.notifications_32dp)
                            }
                        }

                        is UiState.Error -> {

                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchAlmacenItems()
        viewModel.fetchNotifications()
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeConnection()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        requireActivity().menuInflater.inflate(R.menu.menu_almacen_item, menu)
    }

    override fun onItemCreated(item: AlmacenItem) {
        viewModel.createItem(item)
    }

    override fun onItemEdited(item: AlmacenItem) {
        viewModel.editItem(item)
    }

    override fun onItemsTraer(items: Map<AlmacenItem, Int>) {
        viewModel.traer(items)
    }

    override fun onItemsSupply(items: Map<AlmacenItem, Int>) {
        viewModel.supply(items)
    }

    private fun closeDialogs() {
        childFragmentManager.fragments.filterIsInstance<DialogFragment>().forEach { it.dismiss() }
    }
}