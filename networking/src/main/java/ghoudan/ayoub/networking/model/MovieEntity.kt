package ghoudan.ayoub.networking.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieEntity(
    @SerialName("id") val id: Int,
    @SerialName("name") val title: String?,
    @SerialName("overview") val description: String?,
    @SerialName("popularity") val popularity: Double?,
    @SerialName("vote_average") val voteAverage: Double?,
    @SerialName("vote_count") val voteCount: Int?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("first_air_date") val releaseDate: String?)
