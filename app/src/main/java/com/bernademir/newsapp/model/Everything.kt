package com.bernademir.newsapp.model

data class Everything(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)