package com.dev.eraydel.news.models

data class Article(
    var url:String,
    var section:String,
    var title:String,
    var published_date:String,
    var media:List<ArticleMedia>
)
