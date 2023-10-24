package com.gws.ussd.ui.server

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gws.common.utils.UssdNavigation
import dagger.hilt.android.AndroidEntryPoint
import com.gws.ussd.databinding.FragmentDetailsBinding

@AndroidEntryPoint
class ServerFragment : Fragment() {


    private lateinit var binding: FragmentDetailsBinding
    private val movieDetailsViewModel by viewModels<ServerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            val goLogin =
                ServerFragmentDirections.actionServerFragmentToLoginFragment()
            UssdNavigation.navigate(findNavController(), goLogin)
        }
        subscribe()
    }

    private fun subscribe() {
//        movieDetailsViewModel.fetchMovieDetails(fragmentArgs.id)
//            .observe(viewLifecycleOwner) { result ->
//                when (result) {
//                    is ResourceResponse.Loading -> {
//                        (requireActivity() as? MainActivity)?.showLoader()
//                    }
//                    is ResourceResponse.Error -> {
//                        (requireActivity() as? MainActivity)?.hideLoader()
//                        Toast.makeText(
//                            requireContext(),
//                            "Something wrong !",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    is ResourceResponse.Success -> {
//                        (requireActivity() as? MainActivity)?.hideLoader()
//                        result.data?.let { displayMovieDetails(it) }
//                    }
//                }
//            }
    }
}
