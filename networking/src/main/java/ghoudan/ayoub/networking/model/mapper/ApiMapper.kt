package ghoudan.ayoub.networking.model.mapper

import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.networking.model.MovieEntity

internal fun MovieEntity.toAppModel() = Movies(
    id = id,
    title = title,
    description = description,
    popularity = popularity,
    voteAverage = voteAverage,
    voteCount = voteCount,
    posterPath = posterPath,
    releaseDate = releaseDate
)
