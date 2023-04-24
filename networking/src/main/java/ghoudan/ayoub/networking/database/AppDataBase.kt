package ghoudan.ayoub.networking.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ghoudan.ayoub.networking.dao.MoviesDAO
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.converter.Converters

@Database(
    entities = [Movies::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDAO
}
