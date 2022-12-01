package com.fmoreno.pokeapp.adapter

import android.view.View
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity

interface OnItemClickListener {
    fun onItemClick(pokemon: PokemonEntity?, view: View)
}