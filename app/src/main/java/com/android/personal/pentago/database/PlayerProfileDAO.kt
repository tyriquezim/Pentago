package com.android.personal.pentago.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.personal.pentago.model.PlayerProfile

@Dao
interface PlayerProfileDao
{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlayerProfile(playerProfile: PlayerProfile)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlayerProfiles(playerProfiles: List<PlayerProfile>)

    @Query("DELETE FROM PlayerProfile WHERE playerId = :playerId")
    suspend fun deletePlayerProfile(playerId: Int)

    @Query("UPDATE PlayerProfile SET userName = :newUserName, profilePicture = :newProfilePicture, marbleColour = :newMarbleColour WHERE playerId = :targetPlayerId")
    suspend fun updatePlayerProfile(targetPlayerId: Int, newUserName: String, newProfilePicture: String, newMarbleColour: String)

    @Query("SELECT * FROM PlayerProfile WHERE playerId = :playerId")
    suspend fun getPlayerProfile(playerId: Int): PlayerProfile

    @Query("SELECT * FROM PlayerProfile ORDER BY playerId ASC") //Always returns the results in ascending order by ID
    suspend fun getPlayerProfiles(): List<PlayerProfile>
}