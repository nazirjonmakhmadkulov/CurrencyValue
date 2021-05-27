package com.desiredsoftware.currencywatcher.data.api

import com.desiredsoftware.currencywatcher.data.ValCurs
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ServiceBuilder {

    @GET("scripts/XML_daily.asp")
    fun getCurrenciesForDate(@Query("date_req") date: String): Observable<ValCurs>

    companion object ServiceFactory {

        // Создание Logger
        private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        // Создание Custom Interceptor применять Headers
        val headerInterceptor = object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                request = request.newBuilder()
                    .build()
                val response = chain.proceed(request)
                return response
            }
        }

        // Создание OkHttp Client
        private val okHttp = OkHttpClient.Builder()
            .callTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(logger)

        // Создание Retrofit
        fun create(baseUrl: String): ServiceBuilder {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttp.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    SimpleXmlConverterFactory.createNonStrict(
                        Persister(AnnotationStrategy())
                    )
                )
                .build()
            return retrofit.create(ServiceBuilder::class.java)
        }
    }
}