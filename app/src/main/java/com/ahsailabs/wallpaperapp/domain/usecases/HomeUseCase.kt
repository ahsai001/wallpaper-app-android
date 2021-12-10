package com.ahsailabs.wallpaperapp.domain.usecases

import com.ahsailabs.wallpaperapp.domain.irepositories.IWallpaperRepository
import com.ahsailabs.wallpaperapp.domain.states.HomeState
import com.ahsailabs.wallpaperapp.lib.handleProcess

/**
 * Created by ahmad s on 10/12/21.
 */
class HomeUseCase(private val wallpaperRepository: IWallpaperRepository) {
    suspend fun search(homeState: HomeState, query: String, limit: Int) {
        homeState.handleProcess(
            process = {
                wallpaperRepository.search(query, limit)
            },
            isSuccess = {
                it.page ?: -1 > 0
            },
            successData = {
                Pair(it.photos!!, "success fetch data")
            },
            errorMessage = {
                "failed fetch data"
            }
        )
    }
}