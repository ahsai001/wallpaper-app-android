package com.ahsailabs.wallpaperapp.domain.pagings

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahsailabs.wallpaperapp.data.repositories.WallpaperRepository
import com.ahsailabs.wallpaperapp.domain.irepositories.IWallpaperRepository
import com.ahsailabs.wallpaperapp.domain.models.response.Photo

/**
 * Created by ahmad s on 11/12/21.
 */
class WallpaperSource(
    private val wallpaperRepository: IWallpaperRepository,
    private val query: String?,private val limit: Int?
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val nextPage = params.key ?: 1
            val wallpaperResponse = wallpaperRepository.search(query, nextPage, limit)

            if(wallpaperResponse.page ?: -1 > 0) {
                LoadResult.Page(
                    data = wallpaperResponse.photos!!,
                    prevKey = if (nextPage == 1) null else nextPage - 1,
                    nextKey = if (wallpaperResponse.nextPage.isNullOrEmpty()) null else nextPage + 1
                )
            } else {
                LoadResult.Error(RuntimeException("Failed fetch data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return null
    }
}