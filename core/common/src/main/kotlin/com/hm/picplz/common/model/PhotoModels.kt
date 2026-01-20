package com.hm.picplz.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PhotoReview(
    val reviewId: Int,
    val photoReviewUri: String,
    val index: Int,
) : Parcelable

@Parcelize
@Serializable
data class PhotoPortfolio(
    val portfolioId: Int,
    val photoPortfolioUri: String,
    val index: Int,
) : Parcelable
