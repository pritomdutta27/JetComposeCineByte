package com.pritom.data.utils

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.logging.HttpLoggingInterceptor

class CustomHttpLogging(private val logName: String = "OkHttp") : HttpLoggingInterceptor.Logger {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                // Parse and pretty-print the JSON
                val jsonElement = JsonParser.parseString(message)
                val prettyPrintJson = gson.toJson(jsonElement)
                Log.d(logName, prettyPrintJson)
            } catch (e: Exception) {
                // If parsing fails (not a JSON, or malformed), log the original message
                Log.d(logName, message)
            }
        } else {
            // For non-JSON messages (headers, lines, etc.), log as usual
            Log.d(logName, message)
        }
    }
}