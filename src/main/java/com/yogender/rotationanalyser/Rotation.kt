package com.yogender.rotationanalyser

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Rotation(
    val pitch: Float,
    val roll: Float,
    val yaw: Float,
    val time: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)