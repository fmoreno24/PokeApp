package com.fmoreno.pokeapp.dto

import com.fmoreno.pokeapp.model.Pokemon
import com.google.gson.annotations.SerializedName

class ResponsePokemonListDTO {
    @SerializedName("count")
    var count: Long? = null

    @SerializedName("next")
    val next: String? = null

    @SerializedName("previous")
    val previous: String? = null

    @SerializedName("results")
    val results: List<Pokemon>? = null
}