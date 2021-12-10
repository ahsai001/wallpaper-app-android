package com.ahsailabs.wallpaperapp.data.sources

import com.ahsailabs.wallpaperapp.data.ApiService
import com.ahsailabs.wallpaperapp.domain.models.response.SearchResponse
import io.ktor.client.*
import io.ktor.client.request.*

/**
 * Created by ahmad s on 10/12/21.
 */
class WallpaperRemoteSource(httpClient: HttpClient) {
    private val api: HttpClient = httpClient

    suspend fun search(query: String, limit: Int): SearchResponse {
        return api.get(ApiService.searchUrl){
            parameter("query", query)
            parameter("orientation", "portrait")
            parameter("page", 1)
            parameter("per_page", limit)
        }
    }

}