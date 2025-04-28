package com.acon.acon.data.error

import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RemoteErrorCallAdapterFactory(
    private val json: Json = Json
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<Any, Call<Any>>? {
        if (getRawType(returnType) != Call::class.java) return null

        val responseType = (returnType as ParameterizedType).actualTypeArguments[0]
        return RemoteErrorCallAdapter(responseType, json)
    }

    private class RemoteErrorCallAdapter<R>(
        private val responseType: Type, private val json: Json
    ) : CallAdapter<R, Call<R>> {

        override fun responseType(): Type = responseType

        override fun adapt(call: Call<R>): Call<R> {
            return object : Call<R> {
                override fun enqueue(callback: Callback<R>) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (response.isSuccessful) {
                                if (response.body() != null)
                                    callback.onResponse(call, response)
                                else {
                                    callback.onFailure(
                                        call, RemoteError(
                                            statusCode = response.code(),
                                            errorCode = 0,
                                            message = "Empty body",
                                            httpErrorMessage = mapHttpError(response.code())
                                        )
                                    )
                                }
                            } else {
                                val errJson = response.errorBody()?.string()
                                val errResp = try {
                                    errJson?.let { json.decodeFromString<NetworkErrorResponse>(it) }
                                } catch (_: Exception) {
                                    null
                                }
                                callback.onFailure(
                                    call, RemoteError(
                                        statusCode = response.code(),
                                        errorCode = errResp?.code ?: 0,
                                        message = errResp?.message ?: response.message(),
                                        httpErrorMessage = mapHttpError(response.code())
                                    )
                                )
                            }
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            callback.onFailure(call, t)
                        }
                    })
                }

                override fun execute(): Response<R> {
                    val response = call.execute()
                    if (response.isSuccessful) return response
                    val errJson = response.errorBody()?.string()
                    val errResp = try {
                        errJson?.let { json.decodeFromString<NetworkErrorResponse>(it) }
                    } catch (_: Exception) {
                        null
                    }
                    throw RemoteError(
                        statusCode = response.code(),
                        errorCode = errResp?.code ?: 0,
                        message = errResp?.message ?: response.message(),
                        httpErrorMessage = mapHttpError(response.code())
                    )
                }

                override fun clone(): Call<R> = adapt(call.clone())
                override fun isExecuted(): Boolean = call.isExecuted
                override fun cancel() = call.cancel()
                override fun isCanceled(): Boolean = call.isCanceled
                override fun request(): Request = call.request()
                override fun timeout(): Timeout = call.timeout()

                private fun mapHttpError(code: Int) = when (code) {
                    400 -> "Bad Request: 잘못된 요청입니다."
                    401 -> "Unauthorized: 인증되지 않은 사용자입니다."
                    403 -> "Forbidden: 접근 권한이 없습니다."
                    404 -> "Not Found: 요청한 리소스를 찾을 수 없습니다."
                    in 500 until 600 -> "Internal Server Error: 서버 내부 오류입니다."
                    else -> "Unknown Error: 알 수 없는 오류입니다."
                }
            }
        }
    }
}
