package com.gws.networking.converter

import androidx.room.TypeConverter
import com.gws.local_models.models.Movies
import java.lang.reflect.Type
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun getMovieFromString(movieString: String): Movies {
        return Json.decodeFromString(Movies.serializer(), movieString)
    }

    @TypeConverter
    fun getMovieString(movie: Movies): String {
        return Json.encodeToString(Movies.serializer(), movie)
    }

    @TypeConverter
    fun fromMovieListToString(moneys: List<Movies?>?): String? {
        return com.google.gson.Gson().toJson(moneys)
    }

    @TypeConverter
    fun FromStringToMovieList(json: String?): List<Movies?>? {
        if (json == null) return null
        val gson: com.google.gson.Gson = com.google.gson.Gson()
        val type: Type =
            object : com.google.gson.reflect.TypeToken<List<Movies?>?>() {}.getType()
        return gson.fromJson<List<Movies>>(json, type)
    }
}
