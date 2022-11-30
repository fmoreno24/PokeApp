package com.fmoreno.pokeapp.data

import com.fmoreno.pokeapp.data.repositories.IPokemonRepository
import com.fmoreno.pokeapp.di.component.DaggerPokemonComponent
import com.fmoreno.pokeapp.di.module.PokemonModule
import com.fmoreno.pokeapp.dto.ResponsePokemonListDTO
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.model.PokemonResults
import com.fmoreno.pokeapp.model.Species
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface IPokemonInteractor {
    fun getPokemonList(): Observable<PokemonResults>?
    fun getPokemon(id: Int): Observable<Pokemon>?
    fun getSpecies(id: Int): Observable<Species>?
}

class PokemonInteractor : IPokemonInteractor {
    @Inject
    lateinit var pokemonRepository: IPokemonRepository

    init {
        DaggerPokemonComponent
            .builder()
            .pokemonModule(PokemonModule())
            .build()
            .inject(this)
    }

    override fun getPokemonList(): Observable<PokemonResults>? {
        return pokemonRepository
            .getPokemonList()
            ?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                Observable.just(it)
            }
    }

    override fun getPokemon(id: Int): Observable<Pokemon>? {
        return pokemonRepository
            .getPokemon(id)
            ?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                Observable.just(it)
            }
    }

    override fun getSpecies(id: Int): Observable<Species>? {
        return pokemonRepository
            .getSpecies(id)
            ?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                Observable.just(it)
            }
    }
}