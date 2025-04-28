package com.acon.acon.data.error

/**
 * 400, 500번대 에러 발생 시 던져지는 에러
 */
data class RemoteError(
    val statusCode: Int,
    val errorCode: Int,
    override val message: String,
    val httpErrorMessage: String
) : Exception()