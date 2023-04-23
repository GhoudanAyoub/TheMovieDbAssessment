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

    private var moviesLiveData: MutableLiveData<ResourceResponse<List<Movies>>> = MutableLiveData()
    val movies: LiveData<ResourceResponse<List<Movies>>> = moviesLiveData

    fun fetchPopularMovies(pageNumber: Int) {
        viewModelScope.launch {
            moviesRepository.fetchMovies(pageNumber).collect { moviesResult ->
                moviesLiveData.value = moviesResult
            }
        }
    }
}
