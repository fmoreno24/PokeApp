package com.fmoreno.pokeapp.ui

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fmoreno.pokeapp.data.IPokemonInteractor
import com.fmoreno.pokeapp.di.component.DaggerPokemonComponent
import com.fmoreno.pokeapp.di.module.PokemonModule
import com.fmoreno.pokeapp.dto.ResponsePokemonListDTO
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.model.PokemonResults
import com.fmoreno.pokeapp.model.Species
import com.fmoreno.pokeapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel: ViewModel() {
    private var coroutineExceptionHandler: CoroutineExceptionHandler
    private var job: Job = Job()

    val list: MutableList<Pokemon> = mutableListOf()

    private val _myPokemon: MutableLiveData<Result<MutableList<Pokemon>>> = MutableLiveData()
    val myPokemon: LiveData<Result<MutableList<Pokemon>>>
        get() = _myPokemon

    @Inject
    internal lateinit var pokemonInteractor: IPokemonInteractor

    sealed class ViewEvent {
        class QueryGetPokemonListSuccess(val responsePokemonListDTO: PokemonResults): ViewEvent()
        class QueryGetPokemonSuccess(val responsePokemon: Pokemon): ViewEvent()
        class QueryGetSpeciesSuccess(val responseSpecies: Species): ViewEvent()
        class Errors(val errorMessage: String) : ViewEvent()
    }

    var singleLiveEvent: SingleLiveEvent<ViewEvent> =
        SingleLiveEvent()

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _myPokemon.value = Result.Failure(exception)
        }

        DaggerPokemonComponent
            .builder()
            .pokemonModule(PokemonModule())
            .build()
            .inject(this)
    }

    @SuppressLint("CheckResult")
    fun getPokemonList() {
        pokemonInteractor.getPokemonList()?.subscribe({
            singleLiveEvent.value = ViewEvent.QueryGetPokemonListSuccess(it)
        }, {
            singleLiveEvent.value = ViewEvent.Errors("Error! Boom!")
        })
    }

    fun getPokemon(pokemonList: MutableList<Pokemon>) {
        cancelJobIfRunning()
        job = viewModelScope.launch(coroutineExceptionHandler) {
            _myPokemon.value = Result.Loading
            coroutineScope {
                pokemonList.forEach {
                    launch(coroutineExceptionHandler) {
                        if (!checkIfContainsPokemon(list, it)) {
                            list.add(it)
                            viewModelScope.launch {
                                getPokemon(it.id)
                            }
                        }
                    }
                }
            }
            list.sortBy { it.id }
            _myPokemon.value = Result.Success(list)
        }
    }

    private fun checkIfContainsPokemon(pokemonList: MutableList<Pokemon>, pokemon: Pokemon): Boolean {
        pokemonList.forEach {
            if (it.id == pokemon.id) return true
        }
        return false
    }

    @SuppressLint("CheckResult")
    fun getPokemon(id: Int) {
        pokemonInteractor.getPokemon(id)?.subscribe({
            singleLiveEvent.value = ViewEvent.QueryGetPokemonSuccess(it)
        }, {
            singleLiveEvent.value = ViewEvent.Errors("Error! Boom!")
        })
    }

    @SuppressLint("CheckResult")
    fun getSpecies(id: Int) {
        pokemonInteractor.getSpecies(id)?.subscribe({
            singleLiveEvent.value = ViewEvent.QueryGetSpeciesSuccess(it)
        }, {
            singleLiveEvent.value = ViewEvent.Errors("Error! Boom!")
        })
    }

    private fun cancelJobIfRunning() {
        if (job.isActive) {
            job.cancel()
        }
    }

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val value: T) : Result<T>()
        object Loading : Result<Nothing>()
        data class Failure(val throwable: Throwable) : Result<Nothing>()
    }
}