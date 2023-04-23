package ghoudan.ayoub.local_models.models

import ghoudan.ayoub.common.utils.Constant

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
fun Movies.getPictureUrl(): String {
    return "${Constant.IMAGE_BASE_URL}$posterPath"
}
