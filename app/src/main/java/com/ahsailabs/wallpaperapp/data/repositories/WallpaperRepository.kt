package com.ahsailabs.wallpaperapp.data.repositories

import com.ahsailabs.wallpaperapp.data.ApiService
import com.ahsailabs.wallpaperapp.data.sources.WallpaperRemoteSource
import com.ahsailabs.wallpaperapp.domain.irepositories.IWallpaperRepository
import com.ahsailabs.wallpaperapp.domain.models.response.SearchResponse

/**
 * Created by ahmad s on 10/12/21.
 */
class WallpaperRepository(private val wallpaperRemoteSource: WallpaperRemoteSource): IWallpaperRepository {
    override suspend fun search(query: String, limit: Int): SearchResponse {
        return wallpaperRemoteSource.search(query, limit)
    }
}