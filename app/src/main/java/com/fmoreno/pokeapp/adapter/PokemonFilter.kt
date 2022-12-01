package com.fmoreno.pokeapp.adapter

import android.widget.Filter
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity
import java.util.ArrayList

class PokemonFilter (myAdapter: RecyclerViewAdapter, originalList: MutableList<PokemonEntity> ) : Filter(){
    private var recyclerViewAdapter: RecyclerViewAdapter? = myAdapter
    private var originalList: List<PokemonEntity>? = originalList
    private var filteredList: MutableList<PokemonEntity>? = listOf<PokemonEntity>().toMutableList()

    fun PopularFilter(myAdapter: RecyclerViewAdapter?, originalList: List<PokemonEntity>?) {
        recyclerViewAdapter = myAdapter
        this.originalList = originalList
        filteredList = ArrayList<PokemonEntity>()
    }

    override fun performFiltering(charSequence: CharSequence): FilterResults? {
        filteredList!!.clear()
        val results = FilterResults()
        if (charSequence.length == 0) {
            filteredList?.addAll(originalList!!)
        } else {
            val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
            for (pokemon in originalList!!) {
                if (pokemon.name?.lowercase()!!.contains(filterPattern)) {
                    filteredList?.add(pokemon)
                }
            }
        }
        results.values = filteredList
        results.count = filteredList?.size!!
        return results
    }

    override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
        recyclerViewAdapter?.mPokemon?.clear()
        var pokemons: ArrayList<PokemonEntity> = filterResults.values as ArrayList<PokemonEntity>
        if(pokemons!= null && pokemons.size > 0)
            for (pokemon in pokemons){
                recyclerViewAdapter?.mPokemon?.add(pokemon)
            }
        //recyclerViewAdapter?.mMovies?.addAll(filterResults.values as MutableList<Movie?>)
        recyclerViewAdapter?.notifyDataSetChanged()
    }
}