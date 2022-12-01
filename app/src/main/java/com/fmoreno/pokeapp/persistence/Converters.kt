package com.fmoreno.pokeapp.persistence

import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    @JvmStatic
    fun stringToList(data: String?): List<String>? {
        return data?.let {
            it.split(",").map {
                try {
                    it.toString()
                } catch (ex: NumberFormatException) {
                    Log.d(TAG, "Cannot convert $ex to string")
                    null
                }
            }
        }?.filterNotNull()
    }

    @TypeConverter
    @JvmStatic
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}