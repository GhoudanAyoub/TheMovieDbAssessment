package ghoudan.ayoub.movieBest

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.ayoub.movieBest.databinding.ActivityMainBinding
import ghoudan.ayoub.movieBest.ui.favorite.FavoriteViewModel
import ghoudan.ayoub.movieBest.ui.home.HomeViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeFragmentViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
               R.id.navigation_home -> {
                    binding?.navView?.visibility = View.GONE
                }
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
}
