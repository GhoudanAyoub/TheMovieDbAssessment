package com.gws.ussd.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import com.gws.local_models.models.Movies
import com.gws.local_models.models.getPictureUrl
import com.gws.ussd.MainActivity
import com.gws.ussd.databinding.FragmentDetailsBinding
import com.gws.networking.response.ResourceResponse

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {


    private lateinit var binding: FragmentDetailsBinding
    private val movieDetailsViewModel by viewModels<MovieDetailsViewModel>()
    private val fragmentArgs by navArgs<MovieDetailsFragmentArgs>()

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
        subscribe()
    }

    private fun subscribe() {
        movieDetailsViewModel.fetchMovieDetails(fragmentArgs.id)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResourceResponse.Loading -> {
                        (requireActivity() as? MainActivity)?.showLoader()
                    }
                    is ResourceResponse.Error -> {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        Toast.makeText(
                            requireContext(),
                            "Something wrong !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ResourceResponse.Success -> {
                        (requireActivity() as? MainActivity)?.hideLoader()
                        result.data?.let { displayMovieDetails(it) }
                    }
                }
            }
    }

    private fun displayMovieDetails(movie: Movies) {
        binding.movieTitle.text = movie.title
        binding.movieOverview.text = movie.description
        binding.movieReleaseDate.text = movie.releaseDate
        Glide.with(requireContext())
            .load(movie.getPictureUrl())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.moviePoster)
    }
}
