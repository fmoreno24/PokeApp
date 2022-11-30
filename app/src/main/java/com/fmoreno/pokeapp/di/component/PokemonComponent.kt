package com.fmoreno.pokeapp.di.component

import com.fmoreno.pokeapp.data.PokemonInteractor
import com.fmoreno.pokeapp.di.module.PokemonModule
import com.fmoreno.pokeapp.ui.MainViewModel
import dagger.Component

@Component(modules = [PokemonModule::class])
interface PokemonComponent {
    fun inject(pokemonInteractor: PokemonInteractor)
    fun inject(mainViewModel: MainViewModel)
}