package ghoudan.ayoub.movieBest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.repository.MoviesRepository
import ghoudan.ayoub.networking.repository.MoviesRepositoryImpl
import ghoudan.ayoub.networking.response.ResourceResponse
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl
) : ViewModel() {

    private var moviesList = arrayListOf<Movies>()
    private var searchedMoviesList = arrayListOf<Movies>()
    private var moviesLiveData: MutableLiveData<ResourceResponse<List<Movies>>> = MutableLiveData()
    val movies: LiveData<ResourceResponse<List<Movies>>> = moviesLiveData


    private var updatedMovieLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val updatedMovie: LiveData<Boolean> = updatedMovieLiveData

    fun filterMovies(
        pageNumber: Int, query: String
    ) {
        viewModelScope.launch {
            if(query.isNotEmpty())
            moviesRepository.searchMovies(pageNumber, query).collect { moviesResult ->
                if (pageNumber == 1)
                    searchedMoviesList.clear()
                searchedMoviesList.addAll(moviesResult.data ?: listOf())
                moviesRepository.getFavoriteMovies().collect {
                    it.data?.let { favoriteMovies ->
                        moviesResult.data?.map { movie ->
                            movie.isFavorite = favoriteMovies.firstOrNull { movie.id == it.id }!=null
                        }
                    }
                    moviesResult.data = searchedMoviesList
                    moviesLiveData.value = moviesResult
                }
            }
            else
                fetchPopularMovies(pageNumber)
        }
    }

    fun fetchPopularMovies(pageNumber: Int) {
        viewModelScope.launch {
            moviesRepository.fetchMovies(pageNumber).collect { moviesResult ->
                moviesRepository.getFavoriteMovies().asLiveData().value?.data?.let { favoriteMovies ->
                        moviesResult.data?.map { movie ->
                            movie.isFavorite = favoriteMovies.firstOrNull { movie.id == it.id }!=null
                        }
                    }
                    moviesLiveData.value = moviesResult

            }
        }
    }

    fun handleFavoriteMovie(movie: Movies) {
        viewModelScope.launch {
            moviesRepository.addMovieToFavorites(movie)
                .collect {
                    updatedMovieLiveData.value = it
                }
        }
    }
}
