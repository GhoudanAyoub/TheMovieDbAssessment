package ghoudan.ayoub.movieBest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghoudan.ayoub.common.utils.EndlessRecyclerViewScrollListener
import ghoudan.ayoub.common.utils.MoviesVerticalItemDecoration
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.databinding.FragmentHomeBinding
import ghoudan.ayoub.networking.response.ResourceResponse
import ghoudan.ayoub.ui_core.component.MovieListener
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieListener {

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
                moviesListAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                moviesListAdapter.filter.filter(newText)
                return true
            }
        })

        setupMoviesList()
        homeFragmentViewModel.fetchPopularMovies(1)
        homeFragmentViewModel.movies.observe(viewLifecycleOwner) { moviesResult ->
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
                        moviesListAdapter.differ.submitList(it.sortedBy { it.title })
                    }
                }
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
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (moviesListAdapter.getItemViewType(position)) {
                        MoviesListAdapter.TYPE_ITEM -> 1
                        else -> 2
                    }
                }
            }
        }
        binding.movieRecycler.apply {
            adapter = moviesListAdapter
            layoutManager = recyclerViewLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
            addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
                override fun onLoadMore(page: Int) {
                    homeFragmentViewModel.fetchPopularMovies(page)
                }
            })
        }
    }

    override fun onMovieClicked(movie: Movies) {

    }

    override fun onFavoriteClicked(movie: Movies) {

    }

}
