package com.ahsailabs.wallpaperapp.domain.models.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class SearchResponse(
    @SerializedName("next_page")
    val nextPage: String?,
    @SerializedName("prev_page")
    val prevPage: String?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("photos")
    val photos: List<Photo>?,
    @SerializedName("total_results")
    val totalResults: Int?
)