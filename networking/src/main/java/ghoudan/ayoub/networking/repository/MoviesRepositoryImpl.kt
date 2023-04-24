package ghoudan.ayoub.networking.repository

import androidx.room.withTransaction
import dagger.Reusable
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.api.Api
import ghoudan.ayoub.networking.dao.MoviesDAO
import ghoudan.ayoub.networking.database.AppDataBase
import ghoudan.ayoub.networking.model.mapper.toAppModel
import ghoudan.ayoub.networking.response.ResourceResponse
import ghoudan.ayoub.networking.response.networkBoundResource
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

@Reusable
class MoviesRepositoryImpl @Inject constructor(
    private val api: Api,
    private val moviesDao: MoviesDAO,
    private val db: AppDataBase,
) : MoviesRepository {

    override fun searchMovies(
        pageNumber: Int,
        query: String
    ): Flow<ResourceResponse<List<Movies>>> {
        return flow<ResourceResponse<List<Movies>>> {
            val result = api.searchMovies(1, query)
            emit(ResourceResponse.Success(result.data.map { it.toAppModel() }))
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

     fun fetchMovies2(pageNumber: Int): Flow<ResourceResponse<List<Movies>>> {
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

    override fun addMovieToFavorites(movie: Movies): Flow<Boolean> {
        return flow {
            moviesDao.updateFavoriteMovieById(movie.id,movie.isFavorite)
            emit(true)
        }.catch {
            emit(false)
        }.flowOn(Dispatchers.IO)
    }


    override fun fetchMovies(pageNumber: Int) = networkBoundResource(
        query = {
            moviesDao.getMovies(pageNumber.times(10))
        },
        fetch = {
            api.getPopularMovies(pageNumber).data.map { it.toAppModel() }
        },
        saveFetchResult = { movies ->
            db.withTransaction {
                moviesDao.insertMovies(movies)
            }
        }
    )
}
