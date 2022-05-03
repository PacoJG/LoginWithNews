package com.dev.eraydel.news.models

class NYTResponse(
    var status:String,
    var copyright:String,
    var num_results:Int,
    var results:List<Article>
)