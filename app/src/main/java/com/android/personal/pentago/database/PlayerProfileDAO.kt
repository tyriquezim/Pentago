package com.android.personal.pentago.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.personal.pentago.model.PlayerProfile
import com.android.personal.pentago.observers.AchievementObserver

@Dao
interface PlayerProfileDao
{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlayerProfile(playerProfile: PlayerProfile)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlayerProfiles(playerProfiles: List<PlayerProfile>)

    @Query("DELETE FROM PlayerProfile WHERE playerId = :playerId")
    suspend fun deletePlayerProfile(playerId: Int)

    @Query("UPDATE PlayerProfile SET userName = :newUserName, profilePicture = :newProfilePicture, marbleColour = :newMarbleColour, numGamesPlayed = :newNumGamesPlayed, numWins = :newNumWins, numLosses = :newNumLosses, numDraws = :newNumDraws, winPercentage = :newWinPercentage, totalMovesMade = :newNumMovesMade, achievementObserversList = :newAchievementObserverList WHERE playerId = :targetPlayerId")
    suspend fun updatePlayerProfile(targetPlayerId: Int, newUserName: String, newProfilePicture: String, newMarbleColour: String, newNumGamesPlayed: Int, newNumWins: Int, newNumLosses: Int, newNumDraws: Int, newWinPercentage: Double, newNumMovesMade: Int, newAchievementObserverList: List<AchievementObserver>)

    @Query("UPDATE PLayerProfile SET userName = :newUserName WHERE playerId = :targetPlayerId")
    suspend fun updatePlayerProfileUserName(targetPlayerId: Int, newUserName: String)

    @Query("UPDATE PLayerProfile SET profilePicture = :newProfilePicture WHERE playerId = :targetPlayerId")
    suspend fun updatePlayerProfileProfilePicture(targetPlayerId: Int, newProfilePicture: String)

    @Query("UPDATE PLayerProfile SET marbleColour = :newMarbleColour WHERE playerId = :targetPlayerId")
    suspend fun updatePlayerProfileMarbleColour(targetPlayerId: Int, newMarbleColour: String)

    @Query("UPDATE PlayerProfile SET numGamesPlayed = :newNumGamesPlayed, numWins = :newNumWins, numLosses = :newNumLosses, numDraws = :newNumDraws, winPercentage = :newWinPercentage, totalMovesMade = :newNumMovesMade, achievementObserversList = :newAchievementObserverList WHERE playerId = :targetPlayerId")
    suspend fun updatePlayerProfilePlayerStats(targetPlayerId: Int, newNumGamesPlayed: Int, newNumWins: Int, newNumLosses: Int, newNumDraws: Int, newWinPercentage: Double, newNumMovesMade: Int, newAchievementObserverList: List<AchievementObserver>)

    @Query("SELECT * FROM PlayerProfile WHERE playerId = :playerId")
    suspend fun getPlayerProfile(playerId: Int): PlayerProfile

    @Query("SELECT * FROM PlayerProfile ORDER BY playerId ASC") //Always returns the results in ascending order by ID
    suspend fun getPlayerProfiles(): List<PlayerProfile>
}