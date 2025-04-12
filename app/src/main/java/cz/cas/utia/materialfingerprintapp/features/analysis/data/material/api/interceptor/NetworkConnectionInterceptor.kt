package cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.interceptor

import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.exception.NoInternetException
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.helper.NetworkConnectionChecker
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// inspiration from https://dev.to/theplebdev/using-retrofit-interceptors-to-check-network-connection-in-android-and-testing-it-1kl1
class NetworkConnectionInterceptor @Inject constructor(
    private val networkConnectionChecker: NetworkConnectionChecker
) : Interceptor {

    @Throws(NoInternetException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkConnectionChecker.isDeviceOnline()) {
            throw NoInternetException("No internet connection")
        }
        return chain.proceed(chain.request())
    }
}