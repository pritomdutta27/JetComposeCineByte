package com.pritom.data.datasource.remote

import com.pritom.data.model.ApiException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiExceptionCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Check if it's a suspend function returning the expected type
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val responseType = getParameterUpperBound(0, returnType as ParameterizedType)
        return ApiExceptionCallAdapter<Any>(responseType)
    }

    private class ApiExceptionCallAdapter<T>(
        private val responseType: Type
    ) : CallAdapter<T, Call<T>> {

        override fun responseType(): Type = responseType

        override fun adapt(call: Call<T>): Call<T> {
            return ApiExceptionCall(call)
        }
    }

    private class ApiExceptionCall<T>(private val delegate: Call<T>) : Call<T> by delegate {

        override fun execute(): Response<T?> {
            try {
                return delegate.execute()
            } catch (e: HttpException) {
                throw ApiException(
                    statusCode = e.code(),
                    errorBody = e.response()?.errorBody(),
                    message = e.message() ?: "HTTP Error"
                )
            }
        }

    }
}