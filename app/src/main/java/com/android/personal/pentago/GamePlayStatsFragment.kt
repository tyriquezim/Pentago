package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.personal.pentago.databinding.FragmentGamePlayStatsBinding
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class GamePlayStatsFragment : Fragment()
{
    private var _binding: FragmentGamePlayStatsBinding? = null
    private val binding: FragmentGamePlayStatsBinding
        get() = checkNotNull(_binding) {"The FragmentGamePlayStatsBinding instance could not be accessed because it is currently null."}
    private val arguments: GamePlayStatsFragmentArgs by navArgs()
    private lateinit var playerProfile: PlayerProfile

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentGamePlayStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch()
        {
            withContext(Dispatchers.IO)
            {
                PentagoRepository.get().mutex.withLock()
                {
                    playerProfile = PentagoRepository.get().getPlayerProfile(arguments.playerProfileId)
                }
            }
            withContext(Dispatchers.Main)
            {
                binding.apply()
                {
                    totalGamesTextView.setText(playerProfile.playerStats.numGamesPlayed.toString())
                    numberOfWinsTextView.setText(playerProfile.playerStats.numWins.toString())
                    numberOfLossesTextView.setText(playerProfile.playerStats.numLosses.toString())
                    numberOfDrawsTextView.setText(playerProfile.playerStats.numDraws.toString())
                    winPercentageTextView.setText("${String.format("%.2f", playerProfile.playerStats.winPercentage)}%")
                    numberOfMovesTextView.setText(playerProfile.playerStats.totalMovesMade.toString())
                    backButton.setOnClickListener()
                    {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }
}