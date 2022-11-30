package com.fmoreno.pokeapp.adapter

import android.widget.Filter
import com.fmoreno.pokeapp.model.Pokemon
import java.util.ArrayList

class PokemonFilter (myAdapter: RecyclerViewAdapter, originalList: MutableList<Pokemon> ) : Filter(){
    private var recyclerViewAdapter: RecyclerViewAdapter? = myAdapter
    private var originalList: List<Pokemon>? = originalList
    private var filteredList: MutableList<Pokemon>? = listOf<Pokemon>().toMutableList()

    fun PopularFilter(myAdapter: RecyclerViewAdapter?, originalList: List<Pokemon>?) {
        recyclerViewAdapter = myAdapter
        this.originalList = originalList
        filteredList = ArrayList<Pokemon>()
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
        var pokemons: ArrayList<Pokemon> = filterResults.values as ArrayList<Pokemon>
        if(pokemons!= null && pokemons.size > 0)
            for (pokemon in pokemons){
                recyclerViewAdapter?.mPokemon?.add(pokemon)
            }
        //recyclerViewAdapter?.mMovies?.addAll(filterResults.values as MutableList<Movie?>)
        recyclerViewAdapter?.notifyDataSetChanged()
    }
}