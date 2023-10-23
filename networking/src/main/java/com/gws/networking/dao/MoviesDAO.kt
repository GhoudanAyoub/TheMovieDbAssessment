package com.gws.networking.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gws.local_models.models.Movies
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<Movies>)

    @Query("SELECT * FROM movies ORDER BY title")
    suspend fun getMovies(): List<Movies>

    @Query("SELECT * FROM movies WHERE isFavorite == 1 ORDER BY title")
    suspend fun getFavoriteMovies(): List<Movies>

    @Query("SELECT * FROM movies WHERE id=:id")
    suspend fun getMovie(id: Int): Movies

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY title LIMIT :limit")
    suspend fun searchMovies(limit: Int, query: String): List<Movies>

    @Query("UPDATE movies SET isFavorite =:isFavorite WHERE id = :id")
    fun updateFavoriteMovieById(id: Int, isFavorite: Boolean)
}
