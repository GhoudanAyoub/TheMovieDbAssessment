package ghoudan.ayoub.movieBest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.ayoub.common.utils.EndlessRecyclerViewScrollListener
import ghoudan.ayoub.common.utils.MoviesVerticalItemDecoration
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.MainActivity
import ghoudan.ayoub.movieBest.databinding.FragmentHomeBinding
import ghoudan.ayoub.networking.response.ResourceResponse
import ghoudan.ayoub.ui_core.component.MovieListener
import kotlin.math.roundToInt
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieListener {

    private var searchQuery: String = ""
    private lateinit var binding: FragmentHomeBinding
    private val homeFragmentViewModel by viewModels<HomeViewModel>()

    private val moviesListAdapter: MoviesListAdapter by lazy {
        MoviesListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.movieSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchQuery = query
                if (query.isEmpty())
                    homeFragmentViewModel.fetchPopularMovies(1)
                else
                    homeFragmentViewModel.filterMovies(1,query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchQuery = newText
                if (newText.isEmpty())
                    homeFragmentViewModel.fetchPopularMovies(1)
                else
                    homeFragmentViewModel.filterMovies(1,newText)
                return true
            }
        })

        setupMoviesList()
        subscribe()
    }

    private fun subscribe() {
        homeFragmentViewModel.fetchPopularMovies(1)
        homeFragmentViewModel.movies.observe(viewLifecycleOwner) { moviesResult ->
            when (moviesResult) {
                is ResourceResponse.Loading -> {
                    (requireActivity() as? MainActivity)?.showLoader()
                }
                is ResourceResponse.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    Timber.e(moviesResult.error?.message)
                    Toast.makeText(
                        requireContext(),
                        "Something wrong !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ResourceResponse.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    moviesResult.data?.let {
                        moviesListAdapter.setMoviesList(it.sortedBy { it.title })
                        moviesListAdapter.differ.submitList(it.sortedBy { it.title })
                    }
                }
            }
        }
        homeFragmentViewModel.updatedMovie.observe(viewLifecycleOwner) { moviesResult ->
            moviesResult?.let { movie ->
                moviesListAdapter.updateMovie(movie)
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
            clipToPadding = false
            clipChildren = false
            adapter = moviesListAdapter
            layoutManager = recyclerViewLayoutManager
            setHasFixedSize(true)
            if (itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
            binding.movieRecycler.layoutManager?.let { layoutManager ->
                addOnScrollListener(
                    object : EndlessRecyclerViewScrollListener(layoutManager) {
                        override fun onLoadMore(
                            page: Int,
                            take: Int,
                            view: RecyclerView
                        ) {
                            if (searchQuery.isEmpty())
                                homeFragmentViewModel.fetchPopularMovies(page)
                            else
                                homeFragmentViewModel.filterMovies(page, searchQuery)
                        }
                    })
            }
        }
    }

    override fun onMovieClicked(movie: Movies) {
        val action =
            HomeFragmentDirections.actionNavigationHomeToNavigationDetails(movie.id)
        findNavController().navigate(action)
    }

    override fun onFavoriteClicked(movie: Movies) {
        homeFragmentViewModel.handleFavoriteMovie(movie)
    }

}
