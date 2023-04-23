package ghoudan.ayoub.ui_core.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.local_models.models.getPictureUrl
import ghoudan.ayoub.movieBest.ui_core.R
import ghoudan.ayoub.movieBest.ui_core.databinding.SampleMovieViewBinding

class MovieView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val binding = SampleMovieViewBinding.inflate(LayoutInflater.from(context), this)

    private var movieListener: MovieListener? = null
    fun bind(data: MovieViewData) {

        val (movie, listener) = data
        movieListener = listener

        Glide.with(context)
            .load(movie.getPictureUrl())
            .placeholder(R.drawable.ic_new_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.movieImg)

        // Sets the product title based on the language
        binding.movieTitle.text = movie.title
        binding.avgVote.text = movie.voteAverage.toString()
        binding.voteCount.text = movie.voteCount.toString()


        binding.root.setOnClickListener {
            listener.onMovieClicked(movie)
        }
        binding.addQtyBtn.setOnClickListener {
            movie.isFavorite= !movie.isFavorite
            listener.onFavoriteClicked(movie)
        }
    }

    data class MovieViewData(val movie: Movies, val listener: MovieListener)
}
