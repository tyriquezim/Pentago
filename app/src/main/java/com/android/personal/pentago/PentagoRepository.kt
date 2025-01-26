package com.android.personal.pentago

import android.content.Context
import androidx.room.Room
import com.android.personal.pentago.database.PlayerProfileDatabase
import com.android.personal.pentago.model.PlayerProfile

private const val DATABASE_NAME = "player-profile-database"

class PentagoRepository private constructor(context: Context)
{
    private val database: PlayerProfileDatabase = Room.databaseBuilder(context.applicationContext, PlayerProfileDatabase::class.java, DATABASE_NAME).build()

    suspend fun insertPlayerProfile(playerProfile: PlayerProfile) = database.playerProfileDAO().insertPlayerProfile(playerProfile)

    suspend fun insertPlayerProfiles(playerProfiles: List<PlayerProfile>) = database.playerProfileDAO().insertPlayerProfiles(playerProfiles)

    suspend fun deletePlayerProfile(userName: String) = database.playerProfileDAO().deletePlayerProfile(userName)

    suspend fun updatePlayerProfile(targetUserName: String, modelProfile: PlayerProfile) = database.playerProfileDAO().updatePlayerProfile(targetUserName, modelProfile.userName, modelProfile.profilePicture, modelProfile.marbleColour)

    suspend fun getPlayerProfile(userName: String): PlayerProfile = database.playerProfileDAO().getPlayerProfile(userName)

    suspend fun getPlayerProfiles(): List<PlayerProfile> = database.playerProfileDAO().getPlayerProfiles()

    companion object
    {
        private var INSTANCE: PentagoRepository? = null

        fun initialise(context: Context)
        {
            if(INSTANCE == null)
            {
                INSTANCE = PentagoRepository(context)
            }
        }

        fun get(): PentagoRepository
        {
            return INSTANCE ?: throw IllegalStateException("PentagoRepository must be initialised")
        }
    }
}