package ng.novacore.sleezchat.di.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.socket.client.IO
import io.socket.client.Socket
import ng.novacore.sleezchat.BuildConfig
import ng.novacore.sleezchat.di.annotations.MyInterceptor
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.network.ApiInterface
import ng.novacore.sleezchat.network.VerificationService
import ng.novacore.sleezchat.network.interceptors.ConnectivityStatusInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URISyntaxException
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideSocketConnection(sharedPrefHelper: SharedPrefHelper): Socket? {
        try {
            val options: IO.Options = IO.Options()
            options.forceNew = false
            options.timeout = (60*1000)
            options.reconnection = true
            options.reconnectionDelay = 3000
            options.reconnectionDelayMax = 6000
            options.reconnectionAttempts = 99999
            options.query = "Authorization"+sharedPrefHelper.getMyNumber()
            return IO.socket(BuildConfig.MAIN_URL, options)
        } catch (ex: URISyntaxException) {
            ex.printStackTrace()
            throw ex
        }
    }

    @Singleton
    @Provides
    fun provideGSON(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideOkHttp(@MyInterceptor connectivityStatusInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(connectivityStatusInterceptor).build()
    }

    @Provides
    @MyInterceptor
    fun provideConnectivityInterceptor(@ApplicationContext context: Context): Interceptor {
        return ConnectivityStatusInterceptor(
            context
        )
    }

    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.MAIN_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface{
        return  retrofit.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideVerificationService(retrofit: Retrofit): VerificationService{
        return  retrofit.create(VerificationService::class.java)
    }


}