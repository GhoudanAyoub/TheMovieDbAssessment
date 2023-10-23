package com.gws.ui_core.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gws.local_models.models.Movies
import com.gws.local_models.models.Ussd
import com.gws.local_models.models.Ussds
import com.gws.local_models.models.getPictureUrl
import com.gws.ussd.ui_core.R
import com.gws.ussd.ui_core.databinding.SampleMovieViewBinding
import timber.log.Timber

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

        val (ussd) = data


        if(ussd.etat=="1"){
            binding.addQtyBtn.setImageResource(R.drawable.ic_favorite_enabled)
        }else{
            binding.addQtyBtn.setImageResource(R.drawable.ic_favorite)
        }

        binding.movieTitle.text = ussd.id.toString() +" "+ussd.ussd

    }

    data class MovieViewData(val ussd: Ussd)
}
