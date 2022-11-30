package com.fmoreno.pokeapp.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

private var gson: Gson? = null

data class PokemonsResponse(
    @SerializedName("count")
    val count: Long,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<Pokemon>) {

}
/*
data class Result (
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)*/
