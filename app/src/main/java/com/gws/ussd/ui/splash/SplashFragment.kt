package com.gws.ussd.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gws.common.utils.UssdNavigation
import com.gws.networking.providers.CurrentUserProvider
import com.gws.ussd.MainActivity
import com.gws.ussd.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    @Inject
    lateinit var currentUserProvider: CurrentUserProvider

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
            if (currentUserProvider.currentUser() == null) {
                val goLogin =
                    SplashFragmentDirections.actionSplashFragmentToServerFragment()
                UssdNavigation.navigate(findNavController(), goLogin)
            } else {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        }

    }


}
