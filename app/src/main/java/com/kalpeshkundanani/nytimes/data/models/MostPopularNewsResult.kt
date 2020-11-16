package com.kalpeshkundanani.nytimes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Kalpesh Kundanani on 11/11/20.
 */
data class MostPopularNewsResult(
        @Expose
        @SerializedName("results")
        val results: MutableList<NewsResult>? = null
)