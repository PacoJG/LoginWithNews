package com.dev.eraydel.news.listeners

import com.dev.eraydel.news.models.Article

interface OnItemClickListener {
    fun onItemClick(noticia:Article)
}