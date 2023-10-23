package ghoudan.ayoub.movieBest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ghoudan.ayoub.local_models.models.Ussd
import ghoudan.ayoub.movieBest.ui_core.databinding.ItemMovieBinding
import ghoudan.ayoub.ui_core.component.MovieListener
import ghoudan.ayoub.ui_core.component.MovieView

class UssdListAdapter :
    RecyclerView.Adapter<UssdListAdapter.MovieItemViewHolder>() {

    private var UssdList = arrayListOf<Ussd>()
    inner class MovieItemViewHolder(private val movieItemView: ItemMovieBinding) :
        RecyclerView.ViewHolder(movieItemView.root) {
        fun bind(data: Ussd) {
            movieItemView.root.bind(MovieView.MovieViewData(data))
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
        holder.bind(UssdList.distinctBy { it.id }[position])
    }

    override fun getItemCount(): Int {
        return UssdList.distinctBy { it.id }.size
    }

    fun setUssdList(Ussd: List<Ussd>) {
        clearUssdList()
        UssdList.addAll(Ussd)
        notifyDataSetChanged()
    }

    fun clearUssdList() {
        this.UssdList.clear()
    }
}
