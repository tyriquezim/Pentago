package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.personal.pentago.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment()
{
    private var _binding: FragmentMainMenuBinding? = null
    private val binding: FragmentMainMenuBinding
        get() = checkNotNull(_binding) {"The FragmentMainMenuBinding instance could not be accessed because it is currently null."} //Nullable backing property

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false) //The binding property is set in onCreateView to avoid a reference to view being held in memory when the fragments view may not be being displayed
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //This is where the navigation destinations are set for the MainMenuFragment
        binding.apply()
        {
            playButton.setOnClickListener()
            {
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToOpponentSelectFragment())
            }
            profileSettingsButton.setOnClickListener()
            {
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToProfileSettingsFragment())
            }
            gameplayStatsButton.setOnClickListener()
            {
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToStatsProfileSelectFragment())
            }
            achievementsButton.setOnClickListener()
            {
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToAchievementProfileSelectFragment())
            }
            howToPlayButton.setOnClickListener()
            {
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToHowToPlayFragment())
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null //To remove the views references so that it can be freed from memory when the fragment is not visible
    }
}