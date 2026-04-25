package com.example.foodshare

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.foodshare.databinding.ActivityMainBinding
import com.example.foodshare.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        setSupportActionBar(binding.topAppBar)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.signupFragment,
                R.id.homeFragment,
                R.id.donateFragment,
                R.id.profileFragment
            )
        )

        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isAuthScreen =
                destination.id == R.id.loginFragment || destination.id == R.id.signupFragment

            val isDetailsScreen =
                destination.id == R.id.foodDetailsFragment ||
                        destination.id == R.id.manageRequestsFragment

            binding.topAppBar.isVisible = !isAuthScreen
            binding.bottomNav.isVisible = !isAuthScreen && !isDetailsScreen

            supportActionBar?.title = when (destination.id) {
                R.id.homeFragment -> getString(R.string.title_home)
                R.id.donateFragment -> getString(R.string.donate)
                R.id.profileFragment -> getString(R.string.profile)
                R.id.foodDetailsFragment -> getString(R.string.food_details)
                R.id.manageRequestsFragment -> getString(R.string.manage_requests)
                else -> getString(R.string.app_name)
            }

            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_top_app_bar, menu)

        val navController = navHostFragment.navController
        val currentId = navController.currentDestination?.id

        val showMainMenu =
            currentId == R.id.homeFragment ||
                    currentId == R.id.donateFragment ||
                    currentId == R.id.profileFragment

        menu.findItem(R.id.action_manage_requests)?.isVisible = showMainMenu
        menu.findItem(R.id.action_about)?.isVisible = showMainMenu
        menu.findItem(R.id.action_logout)?.isVisible = showMainMenu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = navHostFragment.navController

        return when (item.itemId) {
            R.id.action_manage_requests -> {
                navController.navigate(R.id.manageRequestsFragment)
                true
            }

            R.id.action_logout -> {
                sessionManager.clearSession()
                navController.navigate(R.id.loginFragment)
                true
            }

            R.id.action_about -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}