package com.fmoreno.pokeapp.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * FROM " + PokemonEntity.TABLE_NAME)
    fun getAllPokemon(): LiveData<List<PokemonEntity>>

    @Query("SELECT * FROM " + PokemonEntity.TABLE_NAME + " WHERE id = :id AND info_completa = :info_completa")
    fun getPokemon(id: Int, info_completa: Int): LiveData<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePokemon(pokemonEntity: PokemonEntity)

    @Update
    fun update(pokemonEntity: PokemonEntity)

    @Query("UPDATE " + PokemonEntity.TABLE_NAME + " SET description=:description WHERE id = :id")
    fun updatePokemonForIdReference(description: String?, id: Int)
}