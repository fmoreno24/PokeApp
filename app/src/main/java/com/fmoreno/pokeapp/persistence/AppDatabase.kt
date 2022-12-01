package com.fmoreno.pokeapp.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fmoreno.pokeapp.persistence.dao.PokemonDao
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase(){

    abstract  fun  pokemonDao(): PokemonDao


    companion object{
        private const val DATABASE_NAME = "db_pokemon_app"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}