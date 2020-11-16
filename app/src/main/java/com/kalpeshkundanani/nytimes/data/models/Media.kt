package com.kalpeshkundanani.nytimes.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Media(
        @Expose
        @SerializedName("media-metadata")
        val mediaMetadata: MutableList<MediaMetadatum?>? = null
)