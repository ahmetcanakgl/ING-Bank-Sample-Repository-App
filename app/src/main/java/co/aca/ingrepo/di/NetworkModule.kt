package co.aca.ingrepo.di

import android.util.Log
import co.aca.ingrepo.BuildConfig
import com.google.gson.Gson
import co.aca.ingrepo.base.App
import co.aca.ingrepo.base.AppConfig
import co.aca.ingrepo.repository.UserRepository
import dagger.Module
import dagger.Provides
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttp(configurations: AppConfig,
                               user: UserRepository,
                               app: App
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.e("OkHttp", it)
        })

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        builder.addInterceptor { chain ->
            val originalRequest = chain.request()

            val newRequestBuilder = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .cacheControl(CacheControl.FORCE_NETWORK)

            return@addInterceptor chain.proceed(newRequestBuilder.build())
        }

        builder.addInterceptor(interceptor)
        builder.connectTimeout(configurations.SESSION_TIMEOUT, TimeUnit.SECONDS)
        builder.readTimeout(configurations.SESSION_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(configurations.SESSION_TIMEOUT, TimeUnit.SECONDS)

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true }

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(configurations: AppConfig, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(configurations.apiUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }

    private fun getDeviceInfo(): String {
        return android.os.Build.DEVICE + " " +
                android.os.Build.MODEL + " " +
                android.os.Build.PRODUCT + " " +
                android.os.Build.VERSION.SDK_INT
    }

}