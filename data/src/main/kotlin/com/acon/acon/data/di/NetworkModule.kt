package com.acon.acon.data.di

import android.content.Context
import com.acon.acon.data.BuildConfig
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.error.NetworkErrorResponse
import com.acon.acon.data.error.RemoteError
import com.acon.acon.data.remote.ReissueTokenApi
import com.acon.acon.core.common.Auth
import com.acon.acon.core.common.Naver
import com.acon.acon.core.common.NaverAuthInterceptor
import com.acon.acon.core.common.NoAuth
import com.acon.acon.core.common.ResponseInterceptor
import com.acon.acon.core.common.TokenInterceptor
import com.acon.acon.data.SessionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Naver
    @Singleton
    @Provides
    fun provideNaverClient(
        @NaverAuthInterceptor naverAuthInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.addInterceptor(naverAuthInterceptor)
            .build()
    }

    @Auth
    @Singleton
    @Provides
    fun provideAuthClient(
        @ResponseInterceptor responseInterceptor: Interceptor,
        @TokenInterceptor authInterceptor: Interceptor,
        refreshAuthenticator: Authenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .addInterceptor(responseInterceptor)
            .addInterceptor(authInterceptor)
            .authenticator(refreshAuthenticator)
            .build()
    }

    @NoAuth
    @Singleton
    @Provides
    fun provideNoAuthClient(
        @ResponseInterceptor responseInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.addInterceptor(responseInterceptor)
            .build()
    }

    @Naver
    @Singleton
    @Provides
    fun provideNaverRetrofit(
        @Naver client: OkHttpClient
    ): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Auth
    @Singleton
    @Provides
    fun provideRetrofit(
        @Auth client: OkHttpClient
    ): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @NoAuth
    @Provides
    @Singleton
    fun provideNoAuthRetrofit(
        @NoAuth client: OkHttpClient
    ): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @TokenInterceptor
    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenLocalDataSource: TokenLocalDataSource,
    ): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            runBlocking {
                val accessToken = tokenLocalDataSource.getAccessToken() ?: ""
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
                chain.proceed(newRequest)
            }
        }
    }

    @NaverAuthInterceptor
    @Provides
    @Singleton
    fun provideNaverAuthInterceptor() : Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("x-ncp-apigw-api-key-id", BuildConfig.NAVER_CLIENT_ID)
                .addHeader("x-ncp-apigw-api-key", BuildConfig.NAVER_CLIENT_SECRET)
                .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideRefreshInterceptor(
        @ApplicationContext context: Context,
        tokenLocalDataSource: TokenLocalDataSource,
        reissueTokenApi: ReissueTokenApi,
        sessionManager: SessionManager
    ): Authenticator = AuthAuthenticator(tokenLocalDataSource, reissueTokenApi, sessionManager)

    @ResponseInterceptor
    @Singleton
    @Provides
    fun providesResponseInterceptor() : Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val response = chain.proceed(chain.request())

            if (response.isSuccessful.not()) {  // response 실패 시 실행
                val errorBody = response.body?.string()
                val errorResponse = try {
                    errorBody?.let {
                        Json.decodeFromString<NetworkErrorResponse>(it)
                    }
                } catch (e: Exception) {
                    null
                }

                throw RemoteError(
                    statusCode = response.code,
                    errorCode = errorResponse?.code ?: 0,
                    message = errorResponse?.message ?: response.message,
                    httpErrorMessage = when(response.code) {
                        400 -> "Bad Request: 잘못된 요청입니다."
                        401 -> "Unauthorized: 인증되지 않은 사용자입니다."
                        403 -> "Forbidden: 접근 권한이 없습니다."
                        404 -> "Not Found: 요청한 리소스를 찾을 수 없습니다."
                        in 500 until 600 -> "Internal Server Error: 서버 내부 오류입니다."
                        else -> "Unknown Error: 알 수 없는 오류입니다."
                    },
                )
            }
            response
        }
    }
}
