package com.kalpeshkundanani.nytimes.data.network

import com.kalpeshkundanani.nytimes.data.network.services.MostPopularNewsService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Kalpesh Kundanani on 11/11/20.
 */
class NetworkServicesProvider(baseUrl: String) {
    private val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getMostPopularNewsService(): MostPopularNewsService? {
        return retrofit?.create(MostPopularNewsService::class.java)
    }
}