package com.ahsailabs.wallpaperapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahsailabs.wallpaperapp.domain.models.response.Photo
import com.ahsailabs.wallpaperapp.domain.states.HomeState
import com.ahsailabs.wallpaperapp.domain.usecases.HomeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {
    val homeState = HomeState()

    fun searchWallpapers(query: String?, page: Int?, limit: Int? = 10) {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.search(homeState, query, page, limit)
        }
    }

    var query: String? = ""
    var wallpaperPagerFlow: Flow<PagingData<Photo>> =
        homeUseCase.launchWithFlowPaging(12, {query}).cachedIn(viewModelScope)

    fun fetchPagedWallpapers(query: String?): Boolean {
        if (this.query != query) {
            this.query = query
            return true
        }
        return false
    }

}