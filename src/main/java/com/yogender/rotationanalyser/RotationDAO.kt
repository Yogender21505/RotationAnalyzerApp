package com.yogender.rotationanalyser

import androidx.room.Dao
import androidx.room.Insert

import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RotationDAO {
    @Insert
    fun upsertRotation(rotation: Rotation)

    @Query("SELECT * FROM Rotation")
    fun getAllRotations(): List<Rotation>

    // You can add more queries here as needed
}