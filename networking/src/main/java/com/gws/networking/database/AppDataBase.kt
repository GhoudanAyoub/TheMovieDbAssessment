package com.gws.networking.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gws.networking.dao.MoviesDAO
import com.gws.local_models.models.Movies
import com.gws.networking.converter.Converters

@Database(
    entities = [Movies::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDAO
}
