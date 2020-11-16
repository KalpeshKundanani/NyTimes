package com.kalpeshkundanani.nytimes.data.network.services

import com.kalpeshkundanani.nytimes.data.models.MostPopularNewsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Kalpesh Kundanani on 11/11/20.
 */
interface MostPopularNewsService {
    @GET("/svc/mostpopular/v2/viewed/{period}.json")
    fun fetchMostPopularNews(@Path("period") period: Int, @Query("api-key") apiKey: String?): Call<MostPopularNewsResult?>?
}