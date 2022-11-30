package com.fmoreno.pokeapp.adapter

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fmoreno.pokeapp.R
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.utils.ImageLoadingListener
import com.google.android.material.card.MaterialCardView

class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cvItemPokemon: MaterialCardView? = itemView.findViewById(R.id.cv_item_pokemon)
    var post: ImageView? = itemView.findViewById(R.id.imgView_post)
    var tvIdPokemon: TextView? = itemView.findViewById(R.id.tv_id_pokemon)
    var tvNamePokemon: TextView? = itemView.findViewById(R.id.tv_name_pokemon)
    private var dominantColor: Int = Color.GRAY

    fun bindLaunch(id: Int) {
        val resources = itemView.resources
        setPokemonPoster(id)
        //bindNameAndDate(movie, resources)
    }

    private fun setPokemonPoster(id: Int) {
        //val progressView = itemView.findViewById<View>(R.id.moviePosterProgress).apply { visibility = View.VISIBLE }
        //val loadingListener = ImageLoadingListener(progressView)
        val posterPath = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"

        /*Glide.with(itemView.context.applicationContext)
            .load(posterPath)
            .error(R.drawable.ic_launcher_foreground)
            .fallback(R.drawable.ic_launcher_foreground)
            .listener(loadingListener)
            .into(itemView.findViewById(R.id.imgView_post))*/
        Glide.with(itemView.context.applicationContext)
            .asBitmap()
            .load(posterPath)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {
                        setBgColor(resource)
                    }
                    return false
                }
            })
            .centerCrop()
            .into(itemView.findViewById(R.id.pokemon_image))
    }

    private fun setBgColor(resource: Bitmap) {
        Palette.Builder(resource).generate {
            it?.let { palette ->
                when (post?.context?.resources?.configuration?.uiMode?.and(
                    Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        dominantColor = palette.getMutedColor(Color.GRAY)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        dominantColor = palette.getLightMutedColor(Color.GRAY)
                    }
                }
                cvItemPokemon?.setCardBackgroundColor(dominantColor)
            }
        }
    }

    private fun bindNameAndDate(pokemon: Pokemon, resources: Resources) {
        //itemView.findViewById<TextView>(R.id.title).text = movie.title
    }
}