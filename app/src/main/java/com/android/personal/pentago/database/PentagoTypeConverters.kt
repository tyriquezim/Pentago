package com.android.personal.pentago.database

import androidx.room.TypeConverter
import com.android.personal.pentago.model.PlayerProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PentagoTypeConverters
{
    @TypeConverter
    fun convertPlayerStatisticsToJson(playerStats: PlayerProfile.PlayerStatistics): String
    {
        val gsonObject = Gson()

        return gsonObject.toJson(playerStats)
    }

    @TypeConverter
    fun convertJsonToPlayerStatistics(listJson: String): PlayerProfile.PlayerStatistics
    {
        val gsonObject = Gson()
        val type = object: TypeToken<PlayerProfile.PlayerStatistics>() {}.type

        return gsonObject.fromJson(listJson, type)
    }
}