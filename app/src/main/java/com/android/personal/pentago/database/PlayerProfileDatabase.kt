package com.android.personal.pentago.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.personal.pentago.model.PlayerProfile

@Database(entities = [PlayerProfile::class], version = 1)
abstract class PlayerProfileDatabase: RoomDatabase()
{
    abstract fun playerProfileDAO(): PlayerProfileDao
}