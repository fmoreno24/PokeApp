package com.fmoreno.pokeapp.adapter

import android.view.View
import com.fmoreno.pokeapp.model.Pokemon

interface OnItemClickListener {
    fun onItemClick(pokemon: Pokemon?, view: View)
}