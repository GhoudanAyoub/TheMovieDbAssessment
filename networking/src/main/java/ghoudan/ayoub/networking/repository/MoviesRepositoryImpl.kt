package ghoudan.ayoub.networking.repository

import androidx.room.withTransaction
import dagger.Reusable
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.api.Api
import ghoudan.ayoub.networking.dao.MoviesDAO
import ghoudan.ayoub.networking.database.AppDataBase
import ghoudan.ayoub.networking.model.mapper.toAppModel
import ghoudan.ayoub.networking.offline.NetworkHandler
import ghoudan.ayoub.networking.response.ResourceResponse
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
    private val networkHandler: NetworkHandler
) : MoviesRepository {

    override fun searchMovies(
        pageNumber: Int,
        query: String
    ): Flow<ResourceResponse<List<Movies>>> {
        return flow<ResourceResponse<List<Movies>>> {
            if (networkHandler.checkNetworkAvailability()) {
                val result = api.searchMovies(pageNumber, query)
                val data = result.data.map { it.toAppModel() }
                emit(ResourceResponse.Success(data))
            } else {
                val data = moviesDao.searchMovies(pageNumber.times(30), query)
                emit(ResourceResponse.Success(data))
            }
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

    override fun fetchMovies(pageNumber: Int): Flow<ResourceResponse<List<Movies>>> {
        return flow<ResourceResponse<List<Movies>>> {
            if (networkHandler.checkNetworkAvailability()) {
                val result = api.getPopularMovies(pageNumber)
                db.withTransaction {
                    moviesDao.insertMovies(result.data.map { it.toAppModel() })
                }
                val data = result.data.map { it.toAppModel() }
                data.map {
                    val currentMovie = moviesDao.getMovie(it.id)
                    it.isFavorite = currentMovie.isFavorite
                }
                emit(ResourceResponse.Success(data))
            } else {
                emit(ResourceResponse.Success(moviesDao.getMovies()))
            }
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

    override fun getFavoriteMovies(): Flow<ResourceResponse<List<Movies>>> {
        return flow<ResourceResponse<List<Movies>>> {
            val data = moviesDao.getFavoriteMovies()
            emit(ResourceResponse.Success(data))
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
            .flowOn(Dispatchers.IO)
    }

    override fun fetchMovieDetails(movieId: Int): Flow<ResourceResponse<Movies>> {
        return flow<ResourceResponse<Movies>> {
            if (networkHandler.checkNetworkAvailability()) {
                val result = api.getMovieDetails(movieId)
                val data = result.toAppModel()
                emit(ResourceResponse.Success(data))
            } else {
                val data = moviesDao.getMovie(movieId)
                emit(ResourceResponse.Success(data))
            }
        }.catch { exception ->
            emit(ResourceResponse.Error(exception))
        }.onStart { emit(ResourceResponse.Loading()) }
    }

    override fun addMovieToFavorites(movie: Movies): Flow<Boolean> {
        return flow {
            moviesDao.updateFavoriteMovieById(movie.id, movie.isFavorite)
            emit(true)
        }.catch {
            emit(false)
        }.flowOn(Dispatchers.IO)
    }
}
