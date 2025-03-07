package com.android.personal.pentago

import android.app.Application
import android.util.Log
import androidx.collection.emptyLongSet
import com.android.personal.pentago.model.Marble
import com.android.personal.pentago.model.PlayerProfile
import com.android.personal.pentago.observers.AchievementObserver
import com.android.personal.pentago.observers.DrawAchievementObserver
import com.android.personal.pentago.observers.GamesPlayedAchievementObserver
import com.android.personal.pentago.observers.LoseAchievementObserver
import com.android.personal.pentago.observers.MoveAchievementObserver
import com.android.personal.pentago.observers.WinAchievementObserver
import com.android.personal.pentago.observers.WinPercentageAchievementObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock

//Class that is concerned with the application's lifecycle
class PentagoApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        PentagoRepository.initialise(this) //Instantiated in the Application class so that the repository is only initialised once during the lifetime of the app rather in an activity or fragment which can be destroyed and recreated frequently

        // GlobalScope because this is task must be completed once the app starts running and should not be interrupted.
        GlobalScope.launch()
        {
            Log.d("InitialisePlayers result", initialisePlayers().toString())
        }
    }

    //Function for initiating players. Returns true if it initialised players and false if there were already players in the database.
    suspend fun initialisePlayers(): Boolean
    {
        var werePlayersInitialised: Boolean = false

        val players = PentagoRepository.get().getPlayerProfiles()

        if(players.isEmpty())
        {
            val player1 = PlayerProfile(getString(R.string.player_1_default_username), PlayerProfile.DEFAULT_PP, Marble.RED_MARBLE)
            val player2 = PlayerProfile(getString(R.string.player_2_default_username), PlayerProfile.DEFAULT_PP, Marble.BLUE_MARBLE)
            val aiPlayer = PlayerProfile(getString(R.string.ai_player_name), PlayerProfile.AI_ROBOT_PP, Marble.METALLIC_MARBLE)

            val playerList = listOf(player1, player2, aiPlayer)

            lateinit var achievementObserver: AchievementObserver

            for(player in playerList)
            {
                //Add Draw Observer
                achievementObserver = DrawAchievementObserver()
                achievementObserver.initialiseAchievementMap()
                player.addAchievementObserver(achievementObserver)

                //Add Games Played Observer
                achievementObserver = GamesPlayedAchievementObserver()
                achievementObserver.initialiseAchievementMap()
                player.addAchievementObserver(achievementObserver)

                //Add Lose Observer
                achievementObserver = LoseAchievementObserver()
                achievementObserver.initialiseAchievementMap()
                player.addAchievementObserver(achievementObserver)

                //Add Moves Played Observer
                achievementObserver = MoveAchievementObserver()
                achievementObserver.initialiseAchievementMap()
                player.addAchievementObserver(achievementObserver)

                //Add Win Achievement Observer
                achievementObserver = WinAchievementObserver()
                achievementObserver.initialiseAchievementMap()
                player.addAchievementObserver(achievementObserver)

                //Add Win Percentage Observer
                achievementObserver = WinPercentageAchievementObserver()
                achievementObserver.initialiseAchievementMap()
                player.addAchievementObserver(achievementObserver)
            }

            PentagoRepository.get().insertPlayerProfile(player1)
            PentagoRepository.get().insertPlayerProfile(player2)
            PentagoRepository.get().insertPlayerProfile(aiPlayer)

            werePlayersInitialised = true
        }

        return werePlayersInitialised
    }
}