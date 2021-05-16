package com.bernademir.newsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    val source: Source?,
    val author: String?,
    var title: String?,
    val description: String?,
    val url: String?,
    var urlToImage: String?,
    val publishedAt: String?,
    var content: String?
):Parcelable