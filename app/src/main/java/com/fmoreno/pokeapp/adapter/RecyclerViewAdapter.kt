package com.fmoreno.pokeapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fmoreno.pokeapp.R
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity
import java.util.*

class RecyclerViewAdapter(oItemClickListener: OnItemClickListener, var context: Context) :
    RecyclerView.Adapter<PokemonViewHolder>(),
    Filterable {

    var mPokemon: MutableList<PokemonEntity> = listOf<PokemonEntity>().toMutableList()
    var mFilteredMoviesList: MutableList<PokemonEntity> = listOf<PokemonEntity>().toMutableList()

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

        val pokemon: PokemonEntity = mPokemon.get(position)
        if(isInternetAvailable()){
            holder.bindLaunch(pokemon.id)
        } else {
            holder.post?.setImageResource(R.drawable.pokemon_no_found)
        }

        holder.tvIdPokemon!!.text = "# " + pokemon.id
        holder.tvNamePokemon!!.text = pokemon.name.titlecaseFirstCharIfItIsLowercase()
        holder.cvItemPokemon!!.setOnClickListener {
            onItemClickListener!!.onItemClick(
                pokemon,
                holder.post!!
            )
        }
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
    fun addPokemons(pokemons: MutableList<PokemonEntity>) {
        this.mPokemon.addAll(pokemons)
        notifyDataSetChanged()
    }

    internal fun setPokemons(pokemons: List<PokemonEntity>) {
        this.mPokemon = pokemons.toMutableList()
        notifyDataSetChanged()
    }

    fun isInternetAvailable(): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}