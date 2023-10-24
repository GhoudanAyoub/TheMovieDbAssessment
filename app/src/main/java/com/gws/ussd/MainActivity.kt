package com.gws.ussd

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.gws.common.utils.UssdNavigation
import dagger.hilt.android.AndroidEntryPoint
import com.gws.ussd.databinding.ActivityMainBinding
import com.gws.ussd.ui.home.HomeFragment
import com.gws.ussd.ui.home.HomeViewModel
import com.gws.ussd.ui.login.LoginFragment
import com.gws.ussd.ui.splash.SplashFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggleDrawer: ActionBarDrawerToggle

    private val homeFragmentViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupMenu()
        showFragment(SplashFragment())

        val navDrawer = binding.navigationView
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        navDrawer.setupWithNavController(navController)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.navigation_splash,R.id.navigation_login -> {
//                    binding?.navigationView?.visibility = View.GONE
//                }
//            }
//        }
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(true)
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
            this.setHomeButtonEnabled(true)
        }
    }
    private fun setupMenu() {

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        var navHostFragment = binding.navHostFragmentActivityMain as? NavHostFragment
        var navController = navHostFragment?.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_login, R.id.navigation_splash
            ), drawerLayout
        )
        navController?.let {
            setupActionBarWithNavController(it, appBarConfiguration)
            navView.setupWithNavController(it)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }

    fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken, 0
        )
    }

    fun showLoader() {
        if (binding.loaderOverlay?.visibility == View.GONE) {
            binding.loaderOverlay?.visibility = View.VISIBLE
        }
    }

    fun hideLoader() {
        if (binding.loaderOverlay?.visibility == View.VISIBLE) {
            binding.loaderOverlay?.visibility = View.GONE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    public fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(fragment::class.simpleName).commit()
    }
}
