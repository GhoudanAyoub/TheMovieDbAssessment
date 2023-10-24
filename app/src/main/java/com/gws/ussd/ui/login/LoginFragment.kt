package com.gws.ussd.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gws.ussd.MainActivity
import com.gws.ussd.databinding.FragmentLoginBinding
import com.gws.ussd.ui.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


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
            (requireActivity() as? SplashActivity)?.showLoader()
            viewModel.login(binding.login.text.toString(), binding.password.text.toString())
        }
        subscribe()
    }

    private fun subscribe() {
        viewModel.login.observe(viewLifecycleOwner) {
            if (it) {
                binding.errorMessage.visibility = View.GONE
                lifecycleScope.launchWhenResumed {
                    delay(2000)
                    (requireActivity() as? SplashActivity)?.hideLoader()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            } else {
                (requireActivity() as? SplashActivity)?.hideLoader()
                binding.errorMessage.visibility = View.VISIBLE
            }
        }
    }

}
