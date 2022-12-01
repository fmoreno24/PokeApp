package com.fmoreno.pokeapp.persistence.repositories

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.fmoreno.pokeapp.persistence.AppDatabase
import com.fmoreno.pokeapp.persistence.dao.PokemonDao
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity

class PokemonRepository(application: Application) {
    private val pokemonDao: PokemonDao? =
        AppDatabase.getInstance(application)?.pokemonDao()

    val allPokemons: LiveData<List<PokemonEntity>> = pokemonDao!!.getAllPokemon()


    fun insert(pokemonEntity: PokemonEntity) {
        if (pokemonDao != null) InsertAsyncTask(pokemonDao).execute(pokemonEntity)
    }

    fun updatePokemon(pokemon: PokemonEntity) {
        if (pokemonDao != null) UpdateAsyncTask(pokemonDao).execute(pokemon)
    }

    fun getPokemons(): LiveData<List<PokemonEntity>>? {
        return pokemonDao?.getAllPokemon()
    }

    fun getPokemon(id: Int, info_completa: Int): LiveData<PokemonEntity> {
        return (pokemonDao?.getPokemon(id, info_completa)
            ?: PokemonEntity()) as LiveData<PokemonEntity>
    }

    private class InsertAsyncTask(private val pokemonDao: PokemonDao) :
        AsyncTask<PokemonEntity, Void, Void>() {
        override fun doInBackground(vararg pokemonEntity: PokemonEntity?): Void? {
            for (pokemon in pokemonEntity) {
                if (pokemon != null) pokemonDao.savePokemon(pokemon)
            }
            return null
        }
    }

    private class UpdateAsyncTask(private val pokemonDao: PokemonDao) :
        AsyncTask<PokemonEntity, Void, Void>() {
        override fun doInBackground(vararg pokemons: PokemonEntity?): Void? {
            for (pokemon in pokemons) {
                /*if (pokemon != null) pokemonDao.updatePokemonForIdReference(
                    pokemon.description,
                    pokemon.id!!
                )*/
                if (pokemon != null){
                    pokemonDao.update(pokemon)
                }
            }
            return null
        }
    }
}