package ghoudan.ayoub.networking.repository

import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.model.MovieEntity
import ghoudan.ayoub.networking.response.ResourceResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun fetchMovies(pageNumber: Int): Flow<ResourceResponse<List<Movies>>>
    fun fetchMovieDetails(movieId: Int): Flow<ResourceResponse<Movies>>
}
