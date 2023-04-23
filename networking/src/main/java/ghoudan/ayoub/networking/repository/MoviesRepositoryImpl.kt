package ghoudan.ayoub.networking.repository

import dagger.Reusable
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.api.Api
import ghoudan.ayoub.networking.model.mapper.toAppModel
import ghoudan.ayoub.networking.response.ResourceResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

@Reusable
class MoviesRepositoryImpl @Inject constructor(
    private val api: Api
) : MoviesRepository {

    override fun searchMovies(pageNumber: Int,query: String): Flow<ResourceResponse<List<Movies>>> {
        return flow<ResourceResponse<List<Movies>>> {
            val result = api.searchMovies(1,query)
            emit(ResourceResponse.Success(result.data.map { it.toAppModel() }))
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

    override fun fetchMovies(pageNumber: Int): Flow<ResourceResponse<List<Movies>>> {
        return flow<ResourceResponse<List<Movies>>> {
            val result = api.getPopularMovies(pageNumber)
            emit(ResourceResponse.Success(result.data.map { it.toAppModel() }))
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

    override fun fetchMovieDetails(movieId: Int): Flow<ResourceResponse<Movies>> {
        return flow<ResourceResponse<Movies>> {
            val result = api.getMovieDetails(movieId)
            emit(
                ResourceResponse.Success(result.toAppModel())
            )
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

}
