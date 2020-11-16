package com.kalpeshkundanani.nytimes.data

import com.kalpeshkundanani.nytimes.data.models.MostPopularNewsResult
import com.kalpeshkundanani.nytimes.data.network.services.MostPopularNewsService

import retrofit2.Response
import java.io.IOException

/**
 * Created by Kalpesh Kundanani on 12/11/20.
 */
open class NewsRepository(private val service: MostPopularNewsService, private val apiKey: String) {
    @Throws(IOException::class)
    open fun fetchNews(period: Int): Response<MostPopularNewsResult?>? {
        val mostPopularNewsResultCall = service.fetchMostPopularNews(period, apiKey)
        return mostPopularNewsResultCall?.execute()
    }

    companion object {
        private var newsRepository: NewsRepository? = null
        fun getInstance(service: MostPopularNewsService, apiKey: String): NewsRepository? {
            if (newsRepository == null) {
                newsRepository = NewsRepository(service, apiKey)
            }
            return newsRepository
        }
    }
}