package com.android.personal.pentago.database

import androidx.room.TypeConverter
import com.android.personal.pentago.observers.AchievementObserver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters
{
    @TypeConverter
    fun convertAchievementObserverListToJson(observerList: ArrayList<AchievementObserver>): String
    {
        val gsonObject = Gson()

        return gsonObject.toJson(observerList)
    }

    @TypeConverter
    fun convertJsonToAchievementObserverList(listJson: String): ArrayList<AchievementObserver>
    {
        val gsonObject = Gson()
        val listType = object: TypeToken<ArrayList<AchievementObserver>>() {}.type

        return gsonObject.fromJson(listJson, listType)
    }
}