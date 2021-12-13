package com.ahsailabs.wallpaperapp.domain.irepositories

import com.ahsailabs.wallpaperapp.domain.models.response.SearchResponse

/**
 * Created by ahmad s on 10/12/21.
 */
interface IWallpaperRepository {
    suspend fun search(query: String?, page: Int?, limit: Int?): SearchResponse
}