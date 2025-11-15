package com.pritom.data.helper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pritom.data.model.MovieResponse
import java.io.InputStreamReader

object Helper {
    fun readFileResource(fileName: String): String {
        val inputStream = Helper::class.java.getResourceAsStream(fileName)
        val stringBuilder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            stringBuilder.append(it)
        }
        return stringBuilder.toString()
    }

    fun getTestData(fileName: String): MovieResponse {
        val gson = Gson()
        val jsonString = readFileResource( "/$fileName")
        val typeToken = object : TypeToken<MovieResponse>() {}.type
        return gson.fromJson(jsonString, typeToken)

//        val moshi = Moshi.Builder().build()
//        val typeToken = object : TypeToken<TmdbWrapperModel<Movie>>() {}.type
//        val jsonAdapter= moshi.adapter(typeToken)
//        val jsonString = readFileResource( "/$fileName")
//        return jsonAdapter.fromJson(jsonString)!!
    }
}