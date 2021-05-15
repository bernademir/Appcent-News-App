package com.bernademir.newsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Everything(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
):Parcelable