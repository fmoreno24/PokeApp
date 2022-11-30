package com.fmoreno.pokeapp.di.module

import com.fmoreno.pokeapp.data.IPokemonInteractor
import com.fmoreno.pokeapp.data.PokemonInteractor
import com.fmoreno.pokeapp.data.repositories.IPokemonRepository
import com.fmoreno.pokeapp.data.repositories.PokemonRepository
import dagger.Module
import dagger.Provides

@Module
class PokemonModule {
    @Provides
    fun provideSinisterRepository(): IPokemonRepository {
        return PokemonRepository()
    }

    @Provides
    fun provideSinisterInteractor(): IPokemonInteractor {
        return PokemonInteractor()
    }
}