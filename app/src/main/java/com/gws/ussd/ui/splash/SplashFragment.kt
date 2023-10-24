package com.gws.ussd.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gws.common.utils.UssdNavigation
import com.gws.ussd.MainActivity
import com.gws.ussd.databinding.FragmentSplashBinding
import com.gws.ussd.ui.login.LoginFragment
import com.gws.ussd.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            delay(2000)
            val goLogin =
                SplashFragmentDirections.actionSplashFragmentToNavigationLogin()
            val goHome =
                SplashFragmentDirections.actionNavigationSplashToNavigationHome()
//            UssdNavigation.navigate(findNavController(), goLogin)
            (requireActivity() as? MainActivity)?.showFragment(LoginFragment())
        }

    }


}
