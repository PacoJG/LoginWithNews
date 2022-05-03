package com.dev.eraydel.news.models

import com.google.gson.annotations.SerializedName

data class ArticleMedia(
    var type:String,
    @SerializedName("media-metadata")  var medias:List<MediaMetadata>
)
