package ng.novacore.sleezchat.di.modules

import android.content.Context
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ng.novacore.sleezchat.application.App
import ng.novacore.sleezchat.di.annotations.MyInterceptor
import ng.novacore.sleezchat.network.ApiInterface
import ng.novacore.sleezchat.network.requestResponse.ConnectivityStatusInterceptor
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
    fun provideSocketConnection(): Socket? {
        try {
            return IO.socket("http://192.168.43.205:3000")
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
        return ConnectivityStatusInterceptor(context)
    }

    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("http://192.168.43.205:3000")
            .client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface{
        return  retrofit.create(ApiInterface::class.java)
    }


}