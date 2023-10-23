package com.gws.ui_core.component

import com.gws.local_models.models.Movies


interface MovieListener {
    fun onMovieClicked(movie: Movies)
    fun onFavoriteClicked(movie: Movies)
}
