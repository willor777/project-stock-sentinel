package com.willor.lib_data.data.remote

import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitApi {

    // Google Cloud App Engine Url: https://das-ktstockdata-api.uc.r.appspot.com
    // Heroku Salesforce Cloud Url: https://das-ktstockdata.herokuapp.com/
    const val BASE_URL = "https://das-ktstockdata-api.uc.r.appspot.com"
    private var apiKey = ""

    private val retrofitInstance = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .callTimeout(20000, TimeUnit.MILLISECONDS)
                .followRedirects(true)
                .retryOnConnectionFailure(true)
                .connectionSpecs(
                    listOf(

                        // Secure Connection for User's Credentials
                        // Added 04-21-2023
                        ConnectionSpec.MODERN_TLS

                    )
                )
                .build()
        )
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient()
                    .create()
            )
        )
        .build()

    val stockDataService = retrofitInstance.create(StockDataService::class.java)


    fun setApiKey(apikey: String){
        apiKey = apikey
    }

    fun getApiKey(): String {
        return "Bearer $apiKey"
    }

}
