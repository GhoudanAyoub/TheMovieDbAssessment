package ghoudan.ayoub.networking.di

import android.content.Context
import androidx.room.Room
import ghoudan.ayoub.networking.dao.MoviesDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ghoudan.ayoub.networking.database.AppDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "themoviedb"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteMoviesDAO(appDataBase: AppDataBase): MoviesDAO {
        return appDataBase.moviesDao()
    }
}
