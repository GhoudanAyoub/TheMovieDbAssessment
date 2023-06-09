package ghoudan.ayoub.movieBest.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.MainActivity
import ghoudan.ayoub.movieBest.ui_core.databinding.ItemMovieBinding
import ghoudan.ayoub.ui_core.component.MovieListener
import ghoudan.ayoub.ui_core.component.MovieView
import java.util.Locale

class MoviesListAdapter(val movieListener: MovieListener) :
    RecyclerView.Adapter<MoviesListAdapter.MovieItemViewHolder>() {

    private var moviesList = arrayListOf<Movies>()
    inner class MovieItemViewHolder(private val movieItemView: ItemMovieBinding) :
        RecyclerView.ViewHolder(movieItemView.root) {
        fun bind(data: Movies) {
            movieItemView.root.bind(MovieView.MovieViewData(data, movieListener))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        return MovieItemViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(moviesList.distinctBy { it.id }[position])
    }

    override fun getItemCount(): Int {
        return moviesList.distinctBy { it.id }.size
    }

    fun setMoviesList(movies: List<Movies>) {
        moviesList.addAll(movies)
        notifyDataSetChanged()
    }

    fun clearMoviesList() {
        this.moviesList.clear()
    }
}
