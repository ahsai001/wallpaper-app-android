package com.ahsailabs.wallpaperapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahsailabs.wallpaperapp.domain.states.HomeState
import com.ahsailabs.wallpaperapp.domain.usecases.HomeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {
    val homeState = HomeState()

    fun searchWallpapers(query: String, limit: Int = 50) {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.search(homeState, query, limit)
        }
    }

    private val _wallpapers = MutableLiveData<List<String>>().apply {
        value = listOf(
            "https://images.unsplash.com/photo-1638654757547-6ecb9ff266b9?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8bmF0dXJhbCUyMHZpZXd8ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638915092857-c215b282f759?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fG5hdHVyYWwlMjB2aWV3JTIwbW91bnRhaW58ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638973004937-eb3e693d3682?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8bmF0dXJhbCUyMHZpZXclMjBtb3VudGFpbnxlbnwwfDF8Mnx8&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638973004415-681f2f63d343?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8bmF0dXJhbCUyMHZpZXclMjBtb3VudGFpbnxlbnwwfDF8Mnx8&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638909375533-23eea6119dc4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fG5hdHVyYWwlMjB2aWV3JTIwbW91bnRhaW58ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638654757547-6ecb9ff266b9?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8bmF0dXJhbCUyMHZpZXd8ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638915092857-c215b282f759?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fG5hdHVyYWwlMjB2aWV3JTIwbW91bnRhaW58ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638973004937-eb3e693d3682?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8bmF0dXJhbCUyMHZpZXclMjBtb3VudGFpbnxlbnwwfDF8Mnx8&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638973004415-681f2f63d343?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8bmF0dXJhbCUyMHZpZXclMjBtb3VudGFpbnxlbnwwfDF8Mnx8&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638909375533-23eea6119dc4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fG5hdHVyYWwlMjB2aWV3JTIwbW91bnRhaW58ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638654757547-6ecb9ff266b9?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8bmF0dXJhbCUyMHZpZXd8ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638915092857-c215b282f759?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fG5hdHVyYWwlMjB2aWV3JTIwbW91bnRhaW58ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638973004937-eb3e693d3682?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8bmF0dXJhbCUyMHZpZXclMjBtb3VudGFpbnxlbnwwfDF8Mnx8&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638973004415-681f2f63d343?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8bmF0dXJhbCUyMHZpZXclMjBtb3VudGFpbnxlbnwwfDF8Mnx8&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1638909375533-23eea6119dc4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fG5hdHVyYWwlMjB2aWV3JTIwbW91bnRhaW58ZW58MHwxfDJ8fA%3D%3D&auto=format&fit=crop&w=500&q=60"

        )
    }
    val wallpapers: LiveData<List<String>> = _wallpapers
}