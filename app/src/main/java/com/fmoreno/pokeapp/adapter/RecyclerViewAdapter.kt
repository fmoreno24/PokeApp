package com.fmoreno.pokeapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fmoreno.pokeapp.R
import com.fmoreno.pokeapp.model.Pokemon
import java.util.*

class RecyclerViewAdapter(oItemClickListener: OnItemClickListener) : RecyclerView.Adapter<PokemonViewHolder>(),
    Filterable {
    //private val mMovies: MutableList<Movie> = movies
    var mPokemon: MutableList<Pokemon> = listOf<Pokemon>().toMutableList()
    var mFilteredMoviesList: MutableList<Pokemon> = listOf<Pokemon>().toMutableList()

    private val onItemClickListener: OnItemClickListener? = oItemClickListener
    var mFilter: PokemonFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view: View = LayoutInflater.from(
            parent.context
        ).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val startAnimation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_up)

        val pokemon: Pokemon = mPokemon.get(position)
        val trimmedUrl = pokemon.url?.dropLast(1)
        pokemon.id = trimmedUrl!!.substring(trimmedUrl.lastIndexOf("/") + 1).toInt()
        //holder.post?.setOnClickListener { onItemClickListener!!.onItemClick(pokemon, holder.post!!) }
        holder.bindLaunch(pokemon.id)
        holder.tvIdPokemon!!.text = "# "+ pokemon.id
        holder.tvNamePokemon!!.text = pokemon.name.titlecaseFirstCharIfItIsLowercase()
        holder.cvItemPokemon!!.setOnClickListener { onItemClickListener!!.onItemClick(pokemon, holder.post!!) }
        holder.itemView.startAnimation(startAnimation)
    }

    fun String.titlecaseFirstCharIfItIsLowercase() = replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    override fun getItemCount(): Int {
        return mPokemon.size
    }

    override fun getFilter(): Filter? {
        if (mFilter == null) {
            mFilteredMoviesList.clear()
            mFilteredMoviesList.addAll(
                this.mPokemon
            )
            mFilter = PokemonFilter(this, this.mFilteredMoviesList)
        }
        return mFilter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPokemons(pokemons: MutableList<Pokemon>) {
        this.mPokemon.addAll(pokemons)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPokemon(pokemon: Pokemon) {
        this.mPokemon.add(pokemon)
        notifyDataSetChanged()
    }
}