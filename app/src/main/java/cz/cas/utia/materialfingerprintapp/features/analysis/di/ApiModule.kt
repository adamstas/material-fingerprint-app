package cz.cas.utia.materialfingerprintapp.features.analysis.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.MaterialApiService
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.helper.NetworkConnectionChecker
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.interceptor.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = AppConfig.Server.URL

    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) // todo pak vypnout logy? + kdyz nevypnu, tak to udelat pres Provide

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkConnectionChecker(
        @ApplicationContext appContext: Context // even though this is a singleton, the most recent context will be always provided to this single instance
    ): NetworkConnectionChecker {
        return NetworkConnectionChecker(appContext)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS) // sometimes it takes some time to analyse the image at the server
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkConnectionInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
      //  exceptionMappers: @JvmSuppressWildcards List<HttpExceptionMapper> // todo maybe use later for custom exceptions
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            //.addCallAdapterFactory(ErrorsCallAdapterFactory(exceptionMappers))
            .build()
    }

    @Singleton
    @Provides
    fun provideMaterialApiService(retrofit: Retrofit): MaterialApiService =
        retrofit.create(MaterialApiService::class.java)
}