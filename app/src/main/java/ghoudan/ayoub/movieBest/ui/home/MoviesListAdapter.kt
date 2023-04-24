package ghoudan.ayoub.movieBest.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.ui_core.databinding.ItemMovieBinding
import ghoudan.ayoub.ui_core.component.MovieListener
import ghoudan.ayoub.ui_core.component.MovieView
import java.util.Locale

@SuppressLint("NotifyDataSetChanged")
class MoviesListAdapter(val movieListener: MovieListener) :
    RecyclerView.Adapter<MoviesListAdapter.MovieItemViewHolder>(), Filterable {

    private var moviesList = arrayListOf<Movies>()

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_LOADER = 1
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: ArrayList<Movies> = arrayListOf()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(moviesList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    moviesList.filter { cmd ->
                        cmd.id.toString().lowercase().contains(filterKeyword) ||
                                cmd.title.toString().lowercase().contains(filterKeyword)
                    }.forEach { cmd ->
                        filteredList.add(cmd)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredCmdList = if (results?.values == null) {
                    arrayListOf()
                } else {
                    results.values as ArrayList<Movies>
                }
                moviesList.addAll(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }


    inner class MovieItemViewHolder(private val movieItemView: ItemMovieBinding) :
        RecyclerView.ViewHolder(movieItemView.root) {

        fun bind(data: MovieView.MovieViewData) {
            movieItemView.root.bind(data)
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
        val movieItem = moviesList[position]
        holder.bind(MovieView.MovieViewData(movieItem, movieListener))
    }

    fun setMoviesList(movies: List<Movies>) {
        this.moviesList.clear()
        this.moviesList.addAll(movies)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return moviesList.size + 1
    }

    fun updateMovie(movie: Movies) {
        moviesList.find { it.id == movie.id }?.let { _updatedMovie ->
            notifyItemChanged(moviesList.indexOf(_updatedMovie))
        }
    }
}
