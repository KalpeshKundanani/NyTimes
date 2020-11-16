package com.kalpeshkundanani.nytimes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewsResult(
        @Expose
        @SerializedName("published_date")
        val publishedDate: String? = null,

        @Expose
        @SerializedName("section")
        val section: String? = null,

        @Expose
        @SerializedName("subsection")
        val subsection: String? = null,

        @Expose
        @SerializedName("byline")
        val byline: String? = null,

        @Expose
        @SerializedName("title")
        val title: String? = null,

        @Expose
        @SerializedName("abstract")
        val resultAbstract: String? = null,

        @Expose
        @SerializedName("media")
        val media: MutableList<Media?>? = null
)