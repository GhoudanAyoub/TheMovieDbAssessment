package ghoudan.ayoub.movieBest.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.ayoub.common.utils.EndlessRecyclerViewScrollListener
import ghoudan.ayoub.common.utils.MoviesVerticalItemDecoration
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.databinding.FragmentDashboardBinding
import ghoudan.ayoub.movieBest.databinding.FragmentHomeBinding
import ghoudan.ayoub.movieBest.ui.home.HomeViewModel
import ghoudan.ayoub.movieBest.ui.home.MoviesListAdapter
import ghoudan.ayoub.networking.response.ResourceResponse
import ghoudan.ayoub.ui_core.component.MovieListener
import timber.log.Timber

@AndroidEntryPoint
class FavoriteFragment : Fragment(), MovieListener {

    private lateinit var binding: FragmentDashboardBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private val moviesListAdapter: MoviesListAdapter by lazy {
        MoviesListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
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
                        moviesListAdapter.setMoviesList(it.sortedBy { it.title })
                    }
                }
            }
        }
        favoriteViewModel.updatedMovie.observe(viewLifecycleOwner) { moviesResult ->
            moviesResult?.let { movie ->
                moviesListAdapter.updateMovie(movie)
            }
        }
    }

    private fun setupMoviesList() {
        val itemSpacing =
            resources.getDimension(ghoudan.ayoub.movieBest.ui_core.R.dimen.movie_item_spacing)
        val itemDecoration = MoviesVerticalItemDecoration(itemSpacing.toInt())
        val recyclerViewLayoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        binding.movieRecycler.apply {
            adapter = moviesListAdapter
            layoutManager = recyclerViewLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
        }
    }

    override fun onMovieClicked(movie: Movies) {

    }

    override fun onFavoriteClicked(movie: Movies) {
        favoriteViewModel.handleFavoriteMovie(movie)
    }

}
