package com.fmoreno.pokeapp.network

import com.fmoreno.pokeapp.dto.ResponsePokemonListDTO
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.model.PokemonResults
import com.fmoreno.pokeapp.model.Species
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon/")
    //fun getPokemonList(@Query("limit") limit: Int): Observable<ResponsePokemonListDTO>
    fun getPokemonList(@Query("limit") limit: Int): Observable<PokemonResults>

    @GET("pokemon/{id}")
    fun getPokemon(
        @Path("id") id: Int
    ): Observable<Pokemon>

    @GET("pokemon-species/{id}")
    fun getSpecies(
        @Path("id") id: Int
    ): Observable<Species>
}