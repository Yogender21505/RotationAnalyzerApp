package com.yogender.rotationanalyser

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RotationViewModel(private val dao: RotationDAO):ViewModel() {
    private var rotationUpdateJob: Job? = null
    var pitch_view = mutableStateOf(0f)
    var roll_view = mutableStateOf(0f)
    var yaw_view = mutableStateOf(0f)
    var databaseList: MutableSet<Rotation> = mutableSetOf()
    var loadingProgress =mutableStateOf(0f)
    fun updateDatabase() {
        viewModelScope.launch {
            val repository = RotationRepository(dao)
            repository.updateDatabaseintoRotation(pitch_view,roll_view,yaw_view)
        }
    }

    fun getDatabase(isLoading: MutableState<Boolean>, function: () -> Unit) {
        viewModelScope.launch {
            val repository = RotationRepository(dao)
            repository.getListofDatabase(databaseList,isLoading)

        }
    }

}