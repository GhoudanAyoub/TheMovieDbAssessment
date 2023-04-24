package ghoudan.ayoub.local_models.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import ghoudan.ayoub.common.utils.Constant
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity
data class Movies(
    @PrimaryKey
    val id: Int,
    var title: String?,
    var description : String?,
    var popularity: Double?,
    var voteAverage: Double?,
    var voteCount: Int?,
    var posterPath: String?,
    var releaseDate: String?,
    var isFavorite: Boolean = false
):Parcelable
fun Movies.getPictureUrl(): String {
    return "${Constant.IMAGE_BASE_URL}$posterPath"
}
