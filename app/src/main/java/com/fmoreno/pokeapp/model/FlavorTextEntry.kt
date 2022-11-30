package com.fmoreno.pokeapp.model

import java.io.Serializable

data class FlavorTextEntry(
    val flavor_text: String,
    val language : Language
) : Serializable