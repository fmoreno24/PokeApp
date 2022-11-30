package com.fmoreno.pokeapp.ui

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fmoreno.pokeapp.adapter.OnItemClickListener
import com.fmoreno.pokeapp.adapter.RecyclerViewAdapter
import com.fmoreno.pokeapp.databinding.ActivityMainBinding
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.model.PokemonResults
import com.fmoreno.pokeapp.model.PokemonsResponse
import com.fmoreno.pokeapp.ui.base.BaseActivity
import com.google.gson.Gson

class MainActivity : BaseActivity(), OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var gson: Gson? = null

    private var adapter: RecyclerViewAdapter? = null

    var pokemonDetail: Pokemon? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        //getPokemonList()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setupView(){
        gson = Gson()
        setupViewModel()
        //initialObjects()
        initOperation()
        getPokemonsList()
    }

    /**
     * Obtener listado de pokemones
     */
    private fun getPokemonsList() {
        launchLoading()
        mainViewModel.getPokemonList()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.singleLiveEvent.observe(this, Observer {
            when (it) {
                is MainViewModel.ViewEvent.QueryGetPokemonListSuccess -> validatePokemonList(
                    it.responsePokemonListDTO
                )
                is MainViewModel.ViewEvent.Errors -> launchError()
                else -> {
                    launchError()
                }
            }
        })
    }



    /**
     * Fabian Moreno
     * Inicializador de views y consulta de datos
     */
    private fun initOperation(){
        try{
            adapter = RecyclerViewAdapter(this)

            /*binding.include.rlEmptyView.visibility = View.GONE

            binding.include.ivRefresh.setOnClickListener(){
                binding.include.ivRefresh.visibility = View.GONE
                //getPokemonList()
            }*/

            val params = RelativeLayout.LayoutParams(100, 100)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)

            //binding.rlPokemonList.addView(progressBar, params)
            binding.rvPokemons.layoutManager = LinearLayoutManager(this)
            binding.rvPokemons.adapter = adapter

            //getPokemonList()

            binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    adapter?.getFilter()?.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    adapter?.getFilter()?.filter(newText)
                    return true
                }
            })

        }catch (ex:Exception){
            Log.e("initOperation", ex.toString());
        }
    }

    /**
     * Validación de respuesta de la obtención de pokemones.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    private fun validatePokemonList(response: PokemonResults) {
        /*for(poke in response.results) {
            toLoadList.add(poke)
        }
        if(toLoadList.size > 0){
            //mainViewModel.getPokemon()
            //mainViewModel.getPokemon(toLoadList)
        }*/
        adapter?.addPokemons(response.results as MutableList<Pokemon>)
        hideLoading()
    }

    override fun onItemClick(pokemon: Pokemon?, view: View) {
        pokemonDetail = pokemon
        val datailActivity = Intent(this, PokemonDetailsActivity::class.java)
        datailActivity.putExtra("pokemonDetail", pokemonDetail)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, view, "poster"
        )

        startActivity(datailActivity, options.toBundle())
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}