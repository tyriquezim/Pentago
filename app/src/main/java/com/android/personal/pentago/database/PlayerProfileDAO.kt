package com.android.personal.pentago.database

import androidx.room.Dao
import com.android.personal.pentago.model.PlayerProfile

@Dao
interface PlayerProfileDao
{
    suspend fun insertPlayerProfile(playerProfile: PlayerProfile)

    suspend fun insertPlayerProfiles(playerProfiles: List<PlayerProfile>)

    suspend fun deletePlayerProfile(userName: String)

    suspend fun updatePlayerProfile(userName: String, targetProfile: PlayerProfile) //Make sure to put a check that ensures the PlayerProfile object passed as a parameter has the same username as the entry in the database.

    suspend fun getPlayerProfile(userName: String): PlayerProfile

    suspend fun getPlayerProfiles(): List<PlayerProfile>
}