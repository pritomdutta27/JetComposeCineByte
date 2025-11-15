package com.pritom.data.model

import okhttp3.ResponseBody


class ApiException(
    val statusCode: Int,
    val errorBody: ResponseBody?,
    override val message: String
) : Exception(message)