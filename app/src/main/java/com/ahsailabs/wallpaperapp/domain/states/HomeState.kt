package com.ahsailabs.wallpaperapp.domain.states

import com.ahsailabs.wallpaperapp.domain.models.response.Photo
import com.ahsailabs.wallpaperapp.lib.ProcessState
import com.ahsailabs.wallpaperapp.lib.UseProcessStateFlow

/**
 * Created by ahmad s on 10/12/21.
 */
class HomeState: UseProcessStateFlow<List<Photo>>(ProcessState.Initial())