package com.gws.ussd.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.gws.local_models.models.Movies
import com.gws.networking.repository.MoviesRepository
import com.gws.networking.repository.MoviesRepositoryImpl
import com.gws.networking.response.ResourceResponse
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl
) : ViewModel() {

    private var moviesLiveData: MutableLiveData<ResourceResponse<List<Movies>>> = MutableLiveData()
    val movies: LiveData<ResourceResponse<List<Movies>>> = moviesLiveData


    private var updatedMovieLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val updatedMovie: LiveData<Boolean> = updatedMovieLiveData

    fun getFavoriteMovies() {
        viewModelScope.launch {
            moviesRepository.getFavoriteMovies().collect { moviesResult ->
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
