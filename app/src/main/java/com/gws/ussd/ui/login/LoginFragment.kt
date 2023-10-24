package com.gws.ussd.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gws.common.utils.UssdNavigation
import com.gws.ussd.databinding.FragmentLoginBinding
import com.gws.ussd.ui.splash.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            binding.errorMessage.visibility = View.GONE
            viewModel.login(binding.login.text.toString(), binding.password.text.toString())
        }
        subscribe()
    }

    private fun subscribe() {
        viewModel.login.observe(viewLifecycleOwner) {
            if (it) {
                binding.errorMessage.visibility = View.GONE
                val goHome =
                    LoginFragmentDirections.actionLoginFragmentToNavigationHome()
                UssdNavigation.navigate(findNavController(), goHome)
            }   else {
                binding.errorMessage.visibility = View.VISIBLE
            }
        }
    }

}
