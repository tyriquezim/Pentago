package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.personal.pentago.databinding.FragmentProfileSettingsBinding
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.launch

class ProfileSettingsFragment : Fragment()
{
    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {"The FragmentProfileSettingsBinding instance could not be accessed because it is currently null."}
    private lateinit var player1Profile: PlayerProfile
    private lateinit var player2Profile: PlayerProfile
    /* I decided to created parameters to avoid having to repeatedly access the database as that could be an expensive process. */


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //Calling this in the onViewCreated function to ensure that only the latest versions of the PlayerObjects are used

        binding.apply()
        {
            player1UsernameEditText
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }

    //Function that obtains the player profiles from the database and sets them as parameters.
    private fun getPlayerProfiles()
    {
        lifecycleScope.launch()
        {
            player1Profile = PentagoRepository.get().getPlayerProfile()
            player2Profile = PentagoRepository.get().getPlayerProfile()
        }
    }
}