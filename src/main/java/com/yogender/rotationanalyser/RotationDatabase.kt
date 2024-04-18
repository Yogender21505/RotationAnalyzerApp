package com.yogender.rotationanalyser

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Rotation::class], version = 1)
@TypeConverters(Converters::class)
abstract class RotationDatabase : RoomDatabase() {
    abstract fun rotationDao(): RotationDAO

    companion object {
        @Volatile
        private var INSTANCE: RotationDatabase? = null

        fun getInstance(applicationContext: Context): RotationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    applicationContext.applicationContext,
                    RotationDatabase::class.java,
                    "rotation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
