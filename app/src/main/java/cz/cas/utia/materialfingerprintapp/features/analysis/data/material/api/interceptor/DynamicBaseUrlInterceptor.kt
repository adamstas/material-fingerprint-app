package cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.interceptor

import cz.cas.utia.materialfingerprintapp.features.setting.domain.SettingsRepository
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// for updating each request’s URL to the current one set from Settings screen
class DynamicBaseUrlInterceptor @Inject constructor(
    private val settingsRepository: SettingsRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val baseUrl = settingsRepository.getServerUrlSync()
        val httpUrl = baseUrl.toHttpUrl()

        val newUrl = originalRequest.url.newBuilder()
            .scheme(httpUrl.scheme)
            .host(httpUrl.host)
            .port(httpUrl.port)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
