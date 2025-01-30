package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.personal.pentago.databinding.FragmentStatsProfileSelectBinding
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class StatsProfileSelectFragment : Fragment()
{
    private var _binding: FragmentStatsProfileSelectBinding? = null
    private val binding: FragmentStatsProfileSelectBinding
        get() = checkNotNull(_binding) {"The FragmentStatsProfileSelectBinding instance could not be accessed because it is currently null."} //Nullable backing property

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentStatsProfileSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch()
        {
            lateinit var players: List<PlayerProfile>

            withContext(Dispatchers.IO)
            {
                PentagoRepository.get().mutex.withLock()
                {
                    players = PentagoRepository.get().getPlayerProfiles()
                }
            }
            withContext(Dispatchers.Main)
            {
                binding.apply()
                {

                    player1Button.setOnClickListener()
                    {
                        findNavController().navigate(StatsProfileSelectFragmentDirections.actionStatsProfileSelectFragmentToGamePlayStatsFragment(players.get(0).playerId)) //Again, the way this app is implemented assumes that the PlayerProfile with the lowest ID is player1 and the second lowest is player2
                    }
                    player2Button.setOnClickListener()
                    {
                        findNavController().navigate(StatsProfileSelectFragmentDirections.actionStatsProfileSelectFragmentToGamePlayStatsFragment(players.get(1).playerId))
                    }
                    aiButton.setOnClickListener()
                    {
                        findNavController().navigate(StatsProfileSelectFragmentDirections.actionStatsProfileSelectFragmentToGamePlayStatsFragment(players.get(2).playerId)) //The 3rd profile will always be the AI profile
                    }
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