package com.harsewak.kapp.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.harsewak.kapp.R
import com.harsewak.kapp.api.ServiceManager
import com.harsewak.kapp.api.ServiceManagerImpl
import com.harsewak.kapp.api.request.HeaderInterceptor
import com.harsewak.kapp.data.DataManager
import com.harsewak.kapp.data.LocalDataManager
import com.harsewak.kapp.data.LocalDatabase
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun context(): Context = application.applicationContext

    @Provides
    fun serviceManager(service: Retrofit): ServiceManager = ServiceManagerImpl(service)

    @Provides
    @Singleton
    fun baseUrl(context: Context): String = context.getString(R.string.base_url)

//    @Provides
//    @Singleton
//    fun service(retrofit: Retrofit): Service = retrofit.create(Service::class.java)

    @Provides
    fun retrofit(baseUrl: String, okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(gsonConverterFactory).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()

    @Provides
    @Singleton
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun gson(gsonBuilder: GsonBuilder): Gson = gsonBuilder.create()

    @Provides
    @Singleton
    @GsonNullSerialize
    fun gsonNullSerialize(gsonBuilder: GsonBuilder): Gson = gsonBuilder.serializeNulls().create()

    @Provides
    @Singleton
    fun gsonBuilder(): GsonBuilder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()

    @Provides
    @Singleton
    fun okHttp(headerInterceptor: Interceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(headerInterceptor).connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()


    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun header(dataManager: DataManager): Interceptor = HeaderInterceptor(dataManager.getToken())

    @Provides
    @Singleton
    fun database(context: Context): LocalDatabase = LocalDatabase.create(context)

    @Provides
    @Singleton
    fun dataManager(context: Context, localDatabase: LocalDatabase): DataManager = LocalDataManager(context, localDatabase)


}

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun getServiceManager(): ServiceManager

    fun getDataManager(): DataManager

}

@Qualifier
annotation class GsonNullSerialize