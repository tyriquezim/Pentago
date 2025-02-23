package com.android.personal.pentago.database

import androidx.room.TypeConverter
import com.android.personal.pentago.model.PlayerProfile
import com.android.personal.pentago.observers.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*

class PentagoTypeConverters
{
    val serialiserModule = SerializersModule()
    {
        polymorphic(AchievementObserver::class)
        {
            subclass(DrawAchievementObserver::class)
            subclass(GamesPlayedAchievementObserver::class)
            subclass(LoseAchievementObserver::class)
            subclass(MoveAchievementObserver::class)
            subclass(WinAchievementObserver::class)
            subclass(WinPercentageAchievementObserver::class)
        }
    }

    val jsonFormat = Json() {
        serializersModule = serialiserModule
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun convertAchievementObserverListToJson(observerList: ArrayList<AchievementObserver>): String
    {
        return jsonFormat.encodeToString(observerList)
    }

    @TypeConverter
    fun convertJsonToAchievementObserverList(jsonString: String): ArrayList<AchievementObserver>
    {
        return jsonFormat.decodeFromString(jsonString)
    }
}