package com.ahsailabs.wallpaperapp.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ahsailabs.wallpaperapp.domain.irepositories.IWallpaperRepository
import com.ahsailabs.wallpaperapp.domain.models.response.Photo
import com.ahsailabs.wallpaperapp.domain.pagings.WallpaperSource
import com.ahsailabs.wallpaperapp.domain.states.HomeState
import com.ahsailabs.wallpaperapp.lib.handleProcess
import kotlinx.coroutines.flow.Flow

/**
 * Created by ahmad s on 10/12/21.
 */
class HomeUseCase(private val wallpaperRepository: IWallpaperRepository) {
    suspend fun search(homeState: HomeState, query: String?, page: Int?, limit: Int?) {
        homeState.handleProcess(
            process = {
                wallpaperRepository.search(query, page, limit)
            },
            isSuccess = {
                it.page ?: -1 > 0
            },
            successData = {
                Pair(it, "success fetch data")
            },
            errorMessage = {
                "failed fetch data"
            }
        )
    }

    fun launchWithFlowPaging(
        pageSize: Int,
        getQuery: () -> String?): Flow<PagingData<Photo>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            WallpaperSource(wallpaperRepository, getQuery(), pageSize)
        }.flow
    }
}