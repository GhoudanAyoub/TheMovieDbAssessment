package com.gws.ussd.ui.server

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.gws.local_models.models.Movies
import com.gws.networking.model.ServerEntity
import com.gws.networking.providers.CurrentServerProvider
import com.gws.networking.repository.MoviesRepository
import com.gws.networking.response.ResourceResponse
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ServerViewModel @Inject constructor(
    private var currentServerProvider: CurrentServerProvider
) : ViewModel() {

    fun saveServer(serverEntity: ServerEntity){
        viewModelScope.launch {
            currentServerProvider.saveServer(serverEntity)
        }
    }
}
