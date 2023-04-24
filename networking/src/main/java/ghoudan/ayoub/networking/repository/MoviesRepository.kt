package ghoudan.ayoub.networking.repository

import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.model.MovieEntity
import ghoudan.ayoub.networking.response.ResourceResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun searchMovies(pageNumber: Int,query: String): Flow<ResourceResponse<List<Movies>>>
    fun fetchMovies(pageNumber: Int): Flow<ResourceResponse<List<Movies>>>
    fun fetchMovieDetails(movieId: Int): Flow<ResourceResponse<Movies>>
    fun addMovieToFavorites(movie: Movies): Flow<Boolean>
}
