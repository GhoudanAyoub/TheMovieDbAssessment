package com.gws.networking.api

import com.gws.local_models.models.Ussd
import com.gws.networking.model.MovieEntity
import com.gws.networking.model.UserEntity
import com.gws.networking.request.LoginRequest
import com.gws.networking.request.UpdateUssdRequest
import com.gws.networking.request.UssdRequest
import com.gws.networking.response.ApiResponse
import com.gws.networking.response.Success
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/tv")
    suspend fun searchMovies(
        @Query("page") pageNumber: Int,
        @Query("query") query: String,
        @Query("include_adult") adults: Boolean = false
    ): ApiResponse<List<MovieEntity>>

    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(
        @Query("page") pageNumber: Int
    ): ApiResponse<List<MovieEntity>>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MovieEntity


    @POST("login.php")
    suspend fun Login(@Body loginRequest: LoginRequest): List<UserEntity>
    @POST("ussd.php")
    suspend fun getUssd(@Body ussdRequest: UssdRequest): List<Ussd>
    @POST("updateSuccessUssd.php")
    suspend fun updateSuccessUssd(@Body updateUssdRequest: UpdateUssdRequest): Success
    @POST("updateFailedUssd.php")
    suspend fun updateFailedUssd(@Body updateUssdRequest: UpdateUssdRequest): Success
}
