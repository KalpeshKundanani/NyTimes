package com.kalpeshkundanani.nytimes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MediaMetadatum(
        @Expose
        @SerializedName("url")
        val url: String? = null
)