package ghoudan.ayoub.movieBest.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ghoudan.ayoub.local_models.models.Movies
import ghoudan.ayoub.movieBest.ui_core.databinding.ItemLoaderBinding
import ghoudan.ayoub.movieBest.ui_core.databinding.ItemMovieBinding
import ghoudan.ayoub.ui_core.component.MovieListener
import ghoudan.ayoub.ui_core.component.MovieView
import java.util.Locale

@SuppressLint("NotifyDataSetChanged")
class MoviesListAdapter(val movieListener: MovieListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

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
                    filteredList.addAll(differ.currentList)
                } else {
                    val filterKeyword = constraint.toString()
                        .lowercase(Locale.getDefault()).trim()
                    differ.currentList.filter { cmd ->
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
                differ.submitList(filteredCmdList)
                notifyDataSetChanged()
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Movies>() {
        override fun areItemsTheSame(oldItem: Movies, newItem: Movies) =
            oldItem.id == newItem.id || oldItem.isFavorite == newItem.isFavorite

        override fun areContentsTheSame(oldItem: Movies, newItem: Movies) =
            oldItem == newItem
    }
    val differ = AsyncListDiffer(this, differCallback)


    inner class MovieItemViewHolder(private val movieItemView: ItemMovieBinding) :
        RecyclerView.ViewHolder(movieItemView.root) {

        fun bind(data: MovieView.MovieViewData) {
            movieItemView.root.bind(data)
        }
    }

    inner class LoaderViewHolder(private val loaderItemView: ItemLoaderBinding) :
        RecyclerView.ViewHolder(loaderItemView.root) {
        fun bind() {
            loaderItemView.root.setIndeterminate(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_ITEM -> {
                return MovieItemViewHolder(
                    ItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return LoaderViewHolder(
                    ItemLoaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItemViewHolder -> {
                val movieItem = differ.currentList[position]
                holder.bind(MovieView.MovieViewData(movieItem, movieListener))
            }
            is LoaderViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != differ.currentList.size) {
            TYPE_ITEM
        } else {
            TYPE_LOADER
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size + 1
    }

}
