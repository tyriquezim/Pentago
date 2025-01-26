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

    @Query("DELETE FROM PlayerProfile WHERE userName = :userName")
    suspend fun deletePlayerProfile(userName: String)

    @Query("UPDATE PlayerProfile SET userName = :newUserName, profilePicture = :newProfilePicture, marbleColour = :newMarbleColour WHERE userName = :targetUserName")
    suspend fun updatePlayerProfile(targetUserName: String, newUserName: String, newProfilePicture: String, newMarbleColour: String)

    @Query("SELECT * FROM PlayerProfile WHERE userName = :userName")
    suspend fun getPlayerProfile(userName: String): PlayerProfile

    @Query("SELECT * FROM PlayerProfile")
    suspend fun getPlayerProfiles(): List<PlayerProfile>
}