package ghoudan.ayoub.movieBest.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.ayoub.common.utils.MoviesVerticalItemDecoration
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.databinding.FragmentFavoriteBinding
import ghoudan.ayoub.networking.response.ResourceResponse
import ghoudan.ayoub.ui_core.component.MovieListener
import kotlin.math.roundToInt
import timber.log.Timber

@AndroidEntryPoint
class FavoriteFragment : Fragment(), MovieListener {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel by activityViewModels<FavoriteViewModel>()

    private val favoriteListAdapter: FavoriteListAdapter by lazy {
        FavoriteListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMoviesList()
        subscribe()
    }

    private fun subscribe() {
        favoriteViewModel.getFavoriteMovies()
        favoriteViewModel.movies.observe(viewLifecycleOwner) { moviesResult ->
            when (moviesResult) {
                is ResourceResponse.Loading -> {}
                is ResourceResponse.Error -> {
                    Timber.e(moviesResult.error?.message)
                    Toast.makeText(
                        requireContext(),
                        "Something wrong !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ResourceResponse.Success -> {
                    moviesResult.data?.let {
                        favoriteListAdapter.setList(it)
                    }
                }
            }
        }
        favoriteViewModel.updatedMovie.observe(viewLifecycleOwner) { result ->
            result?.let { result ->
                if (result)
                    favoriteViewModel.getFavoriteMovies()
            }
        }
    }

    private fun setupMoviesList() {
        val itemSpacing =
            resources.getDimension(ghoudan.ayoub.movieBest.ui_core.R.dimen.movie_item_spacing)
        val itemDecoration = MoviesVerticalItemDecoration(2, itemSpacing.roundToInt())
        val recyclerViewLayoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        binding.movieRecycler.apply {
            adapter = favoriteListAdapter
            layoutManager = recyclerViewLayoutManager
            setHasFixedSize(true)
            if (itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
        }
    }

    override fun onMovieClicked(movie: Movies) {
        val action =
            FavoriteFragmentDirections.actionNavigationFavoriteToFragmentDetails(movie.id)
        findNavController().navigate(action)
    }

    override fun onFavoriteClicked(movie: Movies) {
        favoriteViewModel.handleFavoriteMovie(movie)
    }

}
