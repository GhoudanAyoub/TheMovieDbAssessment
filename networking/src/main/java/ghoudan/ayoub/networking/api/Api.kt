package ghoudan.ayoub.networking.api

import ghoudan.ayoub.networking.model.MovieEntity
import ghoudan.ayoub.networking.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int
    ): ApiResponse<List<MovieEntity>>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MovieEntity

}
