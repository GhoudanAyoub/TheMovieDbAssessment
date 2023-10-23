package com.gws.networking.model.mapper

import com.gws.local_models.models.Movies
import com.gws.networking.model.MovieEntity

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
