package ghoudan.ayoub.networking.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ghoudan.ayoub.local_models.models.Movies
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movies>)

    @Query("SELECT * FROM movies ORDER BY title LIMIT :limit")
    fun getMovies(limit:Int): Flow<List<Movies>>

    @Query("UPDATE movies SET isFavorite =:isFavorite WHERE id = :id")
    fun updateFavoriteMovieById(id: Int,isFavorite: Boolean)
}
