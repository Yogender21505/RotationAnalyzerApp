package com.yogender.rotationanalyser

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*



class RotationRepository(private val rotationDao:RotationDAO) {


    suspend fun updateDatabaseintoRotation(
        pitch_view: MutableState<Float>,
        roll_view: MutableState<Float>,
        yaw_view: MutableState<Float>
    ) {
        withContext(Dispatchers.IO) {

            val rotation = Rotation(
                pitch = pitch_view.value,
                roll = roll_view.value,
                yaw = yaw_view.value,
                time = getCurrentTime()
            )
            rotationDao.upsertRotation(rotation)
        }
    }

    suspend fun getListofDatabase(
        databaseList: MutableSet<Rotation>,
        isLoading: MutableState<Boolean>
    ) {
        withContext(Dispatchers.IO) {
            isLoading.value = true
            val rotationsFromDB = rotationDao.getAllRotations()
            databaseList.addAll(rotationsFromDB)

            isLoading.value = false
        }
    }

    private fun getCurrentTime(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTimeMillis)
        return formattedTime
    }

}