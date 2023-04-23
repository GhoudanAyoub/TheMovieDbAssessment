package ghoudan.ayoub.local_models.models

data class Movies(
    val id: Int,
    var title: String?,
    var description : String?,
    var popularity: Double?,
    var voteAverage: Double?,
    var voteCount: Int?,
    var posterPath: String?,
    var releaseDate: String?,
    var isFavorite: Boolean = false)
