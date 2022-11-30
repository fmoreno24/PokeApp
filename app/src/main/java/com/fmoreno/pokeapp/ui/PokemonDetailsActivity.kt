package com.fmoreno.pokeapp.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fmoreno.pokeapp.databinding.ActivityPokemonDetailsBinding
import com.fmoreno.pokeapp.model.FlavorTextEntry
import com.fmoreno.pokeapp.model.Genera
import com.fmoreno.pokeapp.model.Pokemon
import com.fmoreno.pokeapp.model.Species
import com.fmoreno.pokeapp.ui.base.BaseActivity
import com.google.gson.Gson
import java.util.*

class PokemonDetailsActivity: BaseActivity() {
    private lateinit var binding: ActivityPokemonDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    var pokemon: Pokemon? = null
    private var gson: Gson? = null

    private var dominantColor: Int = Color.GRAY

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pokemon = intent.getSerializableExtra("pokemonDetail") as Pokemon?
        setupView()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setupView(){
        gson = Gson()
        setFirstInfoPokemon()
        setupViewModel()
        //initialObjects()
        //initOperation()
        getPokemon()
    }
    /**
     * Obtener información detallada del pokemon
     */
    private fun getPokemon() {
        launchLoading()
        pokemon?.id?.let { mainViewModel.getPokemon(it) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.singleLiveEvent.observe(this, Observer {
            when (it) {
                is MainViewModel.ViewEvent.QueryGetPokemonSuccess -> validatePokemon(
                    it.responsePokemon
                )
                is MainViewModel.ViewEvent.QueryGetSpeciesSuccess -> setInfoPokemon(
                it.responseSpecies
                )
                is MainViewModel.ViewEvent.Errors -> launchError()
                else -> {
                    launchError()
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    private fun validatePokemon(pokemon: Pokemon) {
        //adapter?.addPokemon(pokemon)
        this.pokemon = pokemon
        getSpecies()

    }

    private fun getSpecies() {
        pokemon?.id?.let { mainViewModel.getSpecies(it) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    private fun setInfoPokemon(species: Species) {
        //adapter?.addPokemon(pokemon)
        pokemon?.genera = getPokemonGenera(species.genera)
        pokemon?.description = getPokemonDescription(species.flavor_text_entries)
        pokemon?.capture_rate = species.capture_rate
        pokemon

        binding.cvTypeOne.visibility = View.VISIBLE
        binding.tvTypeOne.text = pokemon!!.types[0].type.name
        if (pokemon?.types?.size!! > 1) {
            binding.cvTypeTwo.visibility = View.VISIBLE
            binding.tvTypeTwo.text = pokemon!!.types[1].type.name
        } else binding.cvTypeTwo.visibility = View.GONE

        binding.tvGenera.text = pokemon?.genera
        binding.tvNotas.text = pokemon?.description
        hideLoading()
    }

    fun setFirstInfoPokemon(){
        binding.mainToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.mainToolbar.titlePokemon.text = pokemon?.name?.titlecaseFirstCharIfItIsLowercase()
        binding.mainToolbar.tvIdPokemonDetails.text = "# " + pokemon?.id
        setImage()
    }
    fun setImage(){
        val posterPath = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon?.id}.png"

        Glide.with(this)
            .asBitmap()
            .load(posterPath)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource != null) {
                        setBgColor(resource)
                    }
                    return false
                }
            })
            .centerCrop()
            .into(binding.pokemonImage)
    }

    private fun setBgColor(resource: Bitmap) {
        Palette.Builder(resource).generate {
            it?.let { palette ->
                when (binding.pokemonImage?.context?.resources?.configuration?.uiMode?.and(
                    Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        dominantColor = palette.getMutedColor(Color.GRAY)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        dominantColor = palette.getLightMutedColor(Color.GRAY)
                    }
                }
                binding.clContent.setBackgroundColor(dominantColor)
            }
        }
    }


    private fun getPokemonGenera(generaList: List<Genera>?): String {
        var index = 0
        while (generaList?.get(index)?.language?.name != "es") {
            index++
        }
        return generaList[index].genus
    }

    private fun getPokemonDescription(flavorTextList: List<FlavorTextEntry>): String {
        var index = flavorTextList.size - 1
        while (flavorTextList[index].language.name != "es") {
            index--
        }
        var flavorText = flavorTextList[index].flavor_text
        flavorText = flavorText.replace("POKéMON", "Pokémon")
        flavorText = flavorText.replace("\n", " ")
        return flavorText
    }
}