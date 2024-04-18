package com.yogender.rotationanalyser
import androidx.room.TypeConverter
import java.sql.Time

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Time? {
        return value?.let { Time(it) }
    }

    @TypeConverter
    fun timeToTimestamp(time: Time?): Long? {
        return time?.time
    }
}
