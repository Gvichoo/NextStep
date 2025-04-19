package com.tbacademy.nextstep.presentation.screen.main

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentMainBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun start() {
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

    override fun listeners() {}
    override fun observers() {}

//    private fun setUpBottomNavigationBar(){
//        val navController = childFragmentManager.findFragmentById(R.id.fragmentContainerView2)
//            ?.findNavController()
//
//        binding.bottomNavigationView.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.nav_home -> {
//                    navController?.navigate(R.id.homeFragment2)
//                    true
//                }
//                R.id.nav_settings -> {
//                    navController?.navigate(R.id.settingsFragment)
//                    true
//                }
//                R.id.nav_profile -> {
//                    navController?.navigate(R.id.profileFragment)
//                    true
//                }
//                R.id.nav_Add -> {
//                    navController?.navigate(R.id.addGoalFragment2)
//                    true
//                }
//                R.id.nav_Notification -> {
//                    navController?.navigate(R.id.notificationFragment)
//                    true
//                }
//                else -> false
//            }
//        }
//    }

//    private fun replaceFragment(fragment: Fragment) {
//        childFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView2, fragment)
//            .commit()
//    }
}