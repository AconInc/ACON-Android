package com.acon.acon.data.error

import java.io.IOException

data class RemoteError(
    val statusCode: Int,
    val errorCode: Int,
    override val message: String,
    val httpErrorMessage: String
) : IOException()