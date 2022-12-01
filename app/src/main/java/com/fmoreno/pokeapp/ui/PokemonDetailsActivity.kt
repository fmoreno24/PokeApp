package com.fmoreno.pokeapp.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fmoreno.pokeapp.R
import com.fmoreno.pokeapp.databinding.ActivityPokemonDetailsBinding
import com.fmoreno.pokeapp.model.*
import com.fmoreno.pokeapp.persistence.entities.PokemonEntity
import com.fmoreno.pokeapp.ui.base.BaseActivity
import com.google.gson.Gson
import java.util.*

class PokemonDetailsActivity: BaseActivity() {
    private lateinit var binding: ActivityPokemonDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    var pokemon: PokemonEntity? = null
    private var gson: Gson? = null

    private var dominantColor: Int = Color.GRAY

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pokemon = intent.getSerializableExtra("pokemonDetail") as PokemonEntity?
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
        appDatabaseViewModel.getPokemon(pokemon!!.id, 1).observe(this, Observer { items ->
            if(items != null){
                setInfoPokemonRoomDB(items)
            } else {
                if(isInternetAvailable(this)){
                    pokemon?.id?.let { mainViewModel.getPokemon(it) }
                } else {
                    hideLoading()
                    launchPopupCancelConfirm("Advertencia!",
                        "No tiene conexión a Internet",
                        "Para poder consultar la información del pokemon debe conectarse a internet.",
                        false)
                }
            }
        })
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
        var pokemonEntity = PokemonEntity()
        pokemonEntity.id = pokemon.id
        pokemonEntity.name = pokemon.name
        pokemonEntity.description = pokemon.description.toString()
        pokemonEntity.sprites = pokemon.sprites.other.official_artwork.front_default
        pokemonEntity.types = getPokemonTypesString(pokemon.types)
        pokemonEntity.base_experience = pokemon.base_experience!!
        pokemonEntity.height = pokemon.height!!
        pokemonEntity.weight = pokemon.weight!!
        this.pokemon = pokemonEntity
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
        pokemon!!.info_completa = 1
        pokemon?.let {
            appDatabaseViewModel.updatePokemon(it)
        }
        hideLoading()
    }

    fun setFirstInfoPokemon(){
        binding.mainToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.mainToolbar.titlePokemon.text = pokemon?.name?.titlecaseFirstCharIfItIsLowercase()
        binding.mainToolbar.tvIdPokemonDetails.text = "# " + pokemon?.id
        if(isInternetAvailable(this)){
            setImage()
        } else {
            binding.pokemonImage.setImageResource(R.drawable.pokemon_no_found)
        }
    }

    private fun setInfoPokemonRoomDB(pokemonEntity: PokemonEntity) {

        binding.mainToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
        if(isInternetAvailable(this)){
            setImage()
        } else {
            binding.pokemonImage.setImageResource(R.drawable.pokemon_no_found)
        }

        binding.mainToolbar.titlePokemon.text = pokemonEntity.name.titlecaseFirstCharIfItIsLowercase()
        binding.mainToolbar.tvIdPokemonDetails.text = "# " + pokemonEntity.id
        binding.cvTypeOne.visibility = View.VISIBLE

        val listType:List<Type> = getPokemonTypes(pokemonEntity.types)
        binding.tvTypeOne.text = listType[0].type.name
        if (listType.size!! > 1) {
            binding.cvTypeTwo.visibility = View.VISIBLE
            binding.tvTypeTwo.text = listType[1].type.name
        } else binding.cvTypeTwo.visibility = View.GONE

        if(pokemonEntity.info_completa == 1){
            binding.tvGenera.visibility = View.VISIBLE
            binding.llContentNotasPokemon.visibility = View.VISIBLE
            binding.llContentInfoBasica.visibility = View.VISIBLE

            binding.tvGenera.text = pokemonEntity.genera
            binding.tvNotas.text = pokemonEntity.description
            binding.tvBaseXp.text = pokemonEntity.base_experience.toString()
            binding.tvHeight.text = "${pokemonEntity.height} cm"
            binding.tvWeight.text = "${pokemonEntity.weight} kg"
        } else {
            binding.tvGenera.visibility = View.GONE
            binding.llContentNotasPokemon.visibility = View.GONE
            binding.llContentInfoBasica.visibility = View.GONE
        }



        hideLoading()
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

    private fun getPokemonTypesString(types: List<Type>) : String {
        val typesString = StringBuilder("")
        for(i in types.indices) {
            typesString.append(types[i].type.name)
            if(i != types.size-1){
                typesString.append(",")
            }
        }
        return typesString.toString()
    }

    private fun getPokemonTypes(typesString: String) : List<Type> {
        val typesStringList = typesString.split(",")
        val typesList = mutableListOf<Type>()
        typesStringList.forEach {
            typesList.add(Type(TypeX(it)))
        }
        return typesList
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