package com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.fragments

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.gestionhotel.modulo_almacen_android.R
import com.cocot3ro.gestionhotel.modulo_almacen_android.databinding.FragmentNotificationsBinding
import com.cocot3ro.gestionhotel.modulo_almacen_android.domain.model.NotificationItem
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.adapters.NotificationsRvAdapter
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.view.states.UiState
import com.cocot3ro.gestionhotel.modulo_almacen_android.ui.viewmodel.NotificationsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private val viewModel: NotificationsViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var rvAdapter: NotificationsRvAdapter
    private lateinit var menuProvider: MenuProvider
    private lateinit var deleteItemTouchHelper: ItemTouchHelper

    private var notifications: List<NotificationItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        rvAdapter = NotificationsRvAdapter(emptyList())

        menuProvider = (object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_notificaciones_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_notificaciones_delete -> {
                        viewModel.deleteNotifications(notifications)
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        })

        deleteItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private val mBackground: ColorDrawable = ColorDrawable()
            private val backgroundColor: Int = requireContext().getColor(R.color.red)
            private val mClearPaint: Paint = Paint()
            private val deleteDrawable: Drawable
            private val intrinsicWidth: Int
            private val intrinsicHeight: Int

            init {
                mClearPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
                deleteDrawable =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.delete_32dp)!!
                intrinsicWidth = deleteDrawable.intrinsicWidth
                intrinsicHeight = deleteDrawable.intrinsicHeight
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val item = rvAdapter.getData()[position]

                viewModel.deleteNotification(item)

                Snackbar.make(
                    binding.root,
                    "Notificación eliminada",
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction("Undo") {
                        viewModel.createNotification(item)
                    }
                }.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val itemHeight = itemView.height

                val isCancelled = dX == 0f && !isCurrentlyActive

                if (isCancelled) {
                    clearCanvas(
                        c,
                        itemView.right + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    )
                    @Suppress("KotlinConstantConditions")
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    return
                }

                mBackground.color = backgroundColor
                mBackground.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                mBackground.draw(c)

                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                deleteDrawable.setBounds(
                    deleteIconLeft,
                    deleteIconTop,
                    deleteIconRight,
                    deleteIconBottom
                )
                deleteDrawable.draw(c)

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            private fun clearCanvas(
                c: Canvas,
                left: Float,
                top: Float,
                right: Float,
                bottom: Float
            ) {
                c.drawRect(left, top, right, bottom, mClearPaint)
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.7f
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        binding.toolbar.setupWithNavController(navController)

        binding.rvNotifications.apply {
            adapter = this@NotificationsFragment.rvAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        deleteItemTouchHelper.attachToRecyclerView(binding.rvNotifications)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        UiState.Loading -> {
                            // TODO
                        }

                        is UiState.Success<*> -> {
                            notifications = (uiState.data as List<*>).map { it as NotificationItem }

                            rvAdapter.updateList(notifications)

                            if (notifications.isEmpty()) {
                                binding.tvEmpty.visibility = View.VISIBLE
                            } else {
                                binding.tvEmpty.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            // TODO
                        }
                    }
                }
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
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchData()
    }
}