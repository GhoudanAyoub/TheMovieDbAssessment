package ghoudan.ayoub.ui_core.component

import ghoudan.ayoub.local_models.models.Movies


interface MovieListener {
    fun onMovieClicked(movie: Movies)
    fun onFavoriteClicked(movie: Movies)
}
