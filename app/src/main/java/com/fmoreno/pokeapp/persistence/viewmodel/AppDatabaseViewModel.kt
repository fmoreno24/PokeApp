package com.fmoreno.pokeapp.persistence.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity
import com.fmoreno.pokeapp.persistence.repositories.AppDatabaseRepository
import com.fmoreno.pokeapp.persistence.repositories.PokemonRepository

class AppDatabaseViewModel (application: Application): AndroidViewModel(application) {
    private val pokemonRepository = PokemonRepository(application)
    private val appDatabaseRepository = AppDatabaseRepository(application)
    val pokemons = pokemonRepository.getPokemons()
    val allItems: LiveData<List<PokemonEntity>>

    init {
        allItems = pokemonRepository.allPokemons
    }
    fun insertPokemon(pokemon: PokemonEntity){
        pokemonRepository.insert(pokemon)
    }

    fun updatePokemon(pokemon: PokemonEntity){
        pokemonRepository.updatePokemon(pokemon)
    }

    @JvmName("getAttachedFiles1")
    fun getPokemons(): LiveData<List<PokemonEntity>>? {
        return pokemonRepository.getPokemons()
    }

    fun getPokemon(id: Int, info_completa: Int): LiveData<PokemonEntity> {
        return pokemonRepository.getPokemon(id, info_completa)
    }

    fun export() {
        appDatabaseRepository.exportDataBase()
    }
}