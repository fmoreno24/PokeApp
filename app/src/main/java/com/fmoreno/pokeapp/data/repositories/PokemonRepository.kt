package com.fmoreno.pokeapp.data.repositories

import com.fmoreno.pokeapp.dto.ResponsePokemonListDTO
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.model.PokemonResults
import com.fmoreno.pokeapp.model.Species
import com.fmoreno.pokeapp.network.ApiFactory
import io.reactivex.Observable

interface IPokemonRepository{
    fun getPokemonList(): Observable<PokemonResults>?
    fun getPokemon(id: Int): Observable<Pokemon>?
    fun getSpecies(id: Int): Observable<Species>?
}

class PokemonRepository : IPokemonRepository {
    override fun getPokemonList(): Observable<PokemonResults>? {
        return ApiFactory.build()
            ?.getPokemonList(150)
    }

    override fun getPokemon(id: Int): Observable<Pokemon>? {
        return ApiFactory.build()
            ?.getPokemon(id)
    }

    override fun getSpecies(id: Int): Observable<Species>? {
        return ApiFactory.build()
            ?.getSpecies(id)
    }

}