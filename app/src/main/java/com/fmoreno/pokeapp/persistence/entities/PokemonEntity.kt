package com.fmoreno.pokeapp.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fmoreno.pokeapp.model.Sprites
import com.fmoreno.pokeapp.model.Stat
import com.fmoreno.pokeapp.model.Type
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = PokemonEntity.TABLE_NAME)
data class PokemonEntity(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: Int = 0,
                         @ColumnInfo(name = "name") @NotNull var name: String = "",
                         @ColumnInfo(name = "sprites") @NotNull var sprites: String = "",
                         @ColumnInfo(name = "stats") @NotNull var stats: String = "",
                         @ColumnInfo(name = "types") @NotNull var types: String = "",
                         @ColumnInfo(name = "base_experience") @NotNull var base_experience: Int = 0,
                         @ColumnInfo(name = "height") @NotNull var height: Int = 0,
                         @ColumnInfo(name = "weight") @NotNull var weight: Int = 0,
                         @ColumnInfo(name = "dominant_color") @NotNull var dominant_color: Int = 0,
                         @ColumnInfo(name = "genera") @NotNull var genera: String = "",
                         @ColumnInfo(name = "description") @NotNull var description: String = "",
                         @ColumnInfo(name = "capture_rate") @NotNull var capture_rate: Int = 0,
                         @ColumnInfo(name = "url") @NotNull var url: String = "",
                         @ColumnInfo(name = "info_completa") @NotNull var info_completa: Int = 0
): Serializable {
    companion object {
        const val TABLE_NAME = "pokemon"
    }
}