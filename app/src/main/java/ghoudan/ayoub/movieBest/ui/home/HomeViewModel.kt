package ghoudan.ayoub.movieBest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.ayoub.local_models.models.Movies
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


    private var updatedMovieLiveData: MutableLiveData<Movies> = MutableLiveData()
    val updatedMovie: LiveData<Movies> = updatedMovieLiveData

    fun filterMovies(
        pageNumber: Int,query: String) {
        viewModelScope.launch {
            moviesRepository.searchMovies(pageNumber, query).collect { moviesResult ->
                if(pageNumber == 1)
                    searchedMoviesList.clear()
                moviesResult.data?.let {
                    it.map { movie ->
                        if (!searchedMoviesList.contains(movie))
                            searchedMoviesList.add(movie)
                    }
                }
                moviesResult.data = searchedMoviesList
                moviesLiveData.value = moviesResult
            }
        }
    }

    fun fetchPopularMovies(pageNumber: Int) {
        viewModelScope.launch {
            moviesRepository.fetchMovies(pageNumber).collect { moviesResult ->
                moviesResult.data?.let {
                    it.map { movie ->
                        if (!moviesList.contains(movie))
                            moviesList.add(movie)
                    }
                }
                moviesResult.data = moviesList
                moviesLiveData.value = moviesResult
            }
        }
    }

    fun handleFavoriteMovie(movie: Movies) {
        viewModelScope.launch {
            moviesRepository.addMovieToFavorites(movie)
                .collect {
                    updatedMovieLiveData.value = movie
                }
        }
    }
}
