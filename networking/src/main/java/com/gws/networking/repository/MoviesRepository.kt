package com.gws.networking.repository

import com.gws.local_models.models.Movies
import com.gws.networking.model.MovieEntity
import com.gws.networking.response.ResourceResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun searchMovies(pageNumber: Int,query: String): Flow<ResourceResponse<List<Movies>>>
    fun fetchMovies(pageNumber: Int): Flow<ResourceResponse<List<Movies>>>
    fun fetchMovieDetails(movieId: Int): Flow<ResourceResponse<Movies>>
    fun getFavoriteMovies(): Flow<ResourceResponse<List<Movies>>>
    fun addMovieToFavorites(movie: Movies): Flow<Boolean>
}
