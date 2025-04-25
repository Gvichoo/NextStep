package com.tbacademy.nextstep.presentation.screen.main.main_screen

import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentMainBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.main_screen.event.MainEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    private val mainViewModel: MainViewModel by viewModels()


    override fun start() {
        setBottomNavigation()
        mainViewModel.onEvent(MainEvent.StartListeningForUnreadNotifications)
    }

    override fun listeners() {}
    override fun observers() {
        observeState()
    }

    private fun observeState() {
        collectLatest(flow = mainViewModel.state) { state ->
            val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.nav_Notification)
            badge.isVisible = state.hasUnreadNotifications
        }
    }

    private fun setBottomNavigation() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        // Add this to prevent error when reelecting the same item
        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId != bottomNavigationView.selectedItemId) {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.bottomNavigationView.removeBadge(R.id.nav_Notification)
    }
}