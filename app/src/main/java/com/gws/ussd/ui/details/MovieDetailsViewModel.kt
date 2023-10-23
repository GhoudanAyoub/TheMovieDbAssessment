package com.gws.ussd.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.gws.local_models.models.Movies
import com.gws.networking.repository.MoviesRepository
import com.gws.networking.response.ResourceResponse
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    fun fetchMovieDetails(movieId: Int): LiveData<ResourceResponse<Movies>> {
        return liveData {
            moviesRepository.fetchMovieDetails(movieId).collect { result ->
                emit(result)
            }
        }
    }
}
