package com.acon.acon.data.di

import android.content.Context
import com.acon.acon.core.common.Auth
import com.acon.acon.core.common.Naver
import com.acon.acon.core.common.NaverAuthInterceptor
import com.acon.acon.core.common.NoAuth
import com.acon.acon.core.common.TokenInterceptor
import com.acon.acon.core.common.UrlConstants
import com.acon.acon.data.BuildConfig
import com.acon.acon.data.SessionManager
import com.acon.acon.data.datasource.local.TokenLocalDataSource
import com.acon.acon.data.error.RemoteErrorCallAdapterFactory
import com.acon.acon.data.remote.ReissueTokenApi
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
            .addInterceptor(authInterceptor)
            .authenticator(refreshAuthenticator)
            .build()
    }

    @NoAuth
    @Singleton
    @Provides
    fun provideNoAuthClient(): OkHttpClient {
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
            .baseUrl(UrlConstants.NAVER_OPEN_API)
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
            .addCallAdapterFactory(RemoteErrorCallAdapterFactory(json))
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
            .addCallAdapterFactory(RemoteErrorCallAdapterFactory(json))
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
}
