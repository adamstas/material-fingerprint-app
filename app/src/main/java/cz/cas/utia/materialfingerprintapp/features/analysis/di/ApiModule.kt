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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = AppConfig.Server.URL

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
        // even though this is a singleton, the most recent context will be always provided to this single instance
        @ApplicationContext appContext: Context
    ): NetworkConnectionChecker {
        return NetworkConnectionChecker(appContext)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            // sometimes it takes some time to analyse the image at the server
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(networkConnectionInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideMaterialApiService(retrofit: Retrofit): MaterialApiService =
        retrofit.create(MaterialApiService::class.java)
}