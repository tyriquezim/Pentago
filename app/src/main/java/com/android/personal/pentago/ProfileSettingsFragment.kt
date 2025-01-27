package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.personal.pentago.databinding.FragmentProfileSettingsBinding
import com.android.personal.pentago.model.Marble
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.GlobalScope
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
        getPlayerProfiles() //Sets the player profile parameters for players 1 and 2

        binding.apply()
        {
            player1UsernameEditText.setText(player1Profile.userName)
            player2UsernameEditText.setText(player2Profile.userName)

            player1UsernameEditText.doOnTextChanged()
            {
                text, _, _, _ -> player1Profile.userName = text.toString() //The underscores are unneeded parameters
            }
            player2UsernameEditText.doOnTextChanged()
            {
                    text, _, _, _ -> player2Profile.userName = text.toString()
            }
            player1AvatarImageView.setOnClickListener()
            {
                findNavController().navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToProfileAvatarSelectFragment())
            }
            player2AvatarImageView.setOnClickListener()
            {
                findNavController().navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToProfileAvatarSelectFragment())
            }
            player1MarbleColourImageView.setOnClickListener()
            {
                findNavController().navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToMarbleColourSelectFragment())
            }
            player2MarbleColourImageView.setOnClickListener()
            {
                findNavController().navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToMarbleColourSelectFragment())
            }
            helpButton.setOnClickListener()
            {
                profileSettingHelpDesc.visibility = View.VISIBLE
            }
            backButton.setOnClickListener()
            {
                findNavController().popBackStack()
            }

            //Setting the Player Avatars

            //Player 1 profile picture logic
            when(player1Profile.profilePicture)
            {
                PlayerProfile.ANDROID_ROBOT_PP -> player1AvatarImageView.setImageResource(R.drawable.android_robot_profile_picture)
                PlayerProfile.BEACH_PP -> player1AvatarImageView.setImageResource(R.drawable.beach_profile_picture)
                PlayerProfile.DEFAULT_PP -> player1AvatarImageView.setImageResource(R.drawable.default_avatar)
                PlayerProfile.DESERT_PP -> player1AvatarImageView.setImageResource(R.drawable.desert_profile_picture)
                PlayerProfile.GIRAFFE_PP -> player1AvatarImageView.setImageResource(R.drawable.giraffe_profile_picture)
                PlayerProfile.LION_PP -> player1AvatarImageView.setImageResource(R.drawable.lion_profile_picture)
                PlayerProfile.MOUNTAIN_PP -> player1AvatarImageView.setImageResource(R.drawable.mountain_profile_picture)
                PlayerProfile.OSTRICH_PP -> player1AvatarImageView.setImageResource(R.drawable.ostrich_profile_picture)
                PlayerProfile.TIGER_PP -> player1AvatarImageView.setImageResource(R.drawable.tiger_profile_picture)
                PlayerProfile.TREE_PP -> player1AvatarImageView.setImageResource(R.drawable.tree_profile_picture)
                PlayerProfile.ZEBRA_PP -> player1AvatarImageView.setImageResource(R.drawable.zebra_profile_picture)
                else -> throw IllegalArgumentException("There is no valid image resource for Player 1 profile picture ${player1Profile.profilePicture}")
            }

            //Player 2 profile picture logic
            when(player2Profile.profilePicture)
            {
                PlayerProfile.ANDROID_ROBOT_PP -> player2AvatarImageView.setImageResource(R.drawable.android_robot_profile_picture)
                PlayerProfile.BEACH_PP -> player2AvatarImageView.setImageResource(R.drawable.beach_profile_picture)
                PlayerProfile.DEFAULT_PP -> player2AvatarImageView.setImageResource(R.drawable.default_avatar)
                PlayerProfile.DESERT_PP -> player2AvatarImageView.setImageResource(R.drawable.desert_profile_picture)
                PlayerProfile.GIRAFFE_PP -> player2AvatarImageView.setImageResource(R.drawable.giraffe_profile_picture)
                PlayerProfile.LION_PP -> player2AvatarImageView.setImageResource(R.drawable.lion_profile_picture)
                PlayerProfile.MOUNTAIN_PP -> player2AvatarImageView.setImageResource(R.drawable.mountain_profile_picture)
                PlayerProfile.OSTRICH_PP -> player2AvatarImageView.setImageResource(R.drawable.ostrich_profile_picture)
                PlayerProfile.TIGER_PP -> player2AvatarImageView.setImageResource(R.drawable.tiger_profile_picture)
                PlayerProfile.TREE_PP -> player2AvatarImageView.setImageResource(R.drawable.tree_profile_picture)
                PlayerProfile.ZEBRA_PP -> player2AvatarImageView.setImageResource(R.drawable.zebra_profile_picture)
                else -> throw IllegalArgumentException("There is no valid image resource for Player 2 profile picture ${player2Profile.profilePicture}")
            }

            //Setting the Marble Colours

            //Player 1 Marble colour logic
            when(player1Profile.marbleColour)
            {
                Marble.RED_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.red_baseline_circle_24)
                Marble.ORANGE_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.orange_baseline_circle_24)
                Marble.YELLOW_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.yellow_baseline_circle_24)
                Marble.GREEN_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.green_baseline_circle_24)
                Marble.BLUE_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.blue_baseline_circle_24)
                Marble.PURPLE_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.purple_baseline_circle_24)
                Marble.PINK_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.pink_baseline_circle_24)
                Marble.BLACK_MARBLE -> player1MarbleColourImageView.setImageResource(R.drawable.black_baseline_circle_24)
                else -> throw IllegalArgumentException("There is no valid image resource for Player 1 marble colour ${player1Profile.marbleColour}")
            }

            //Player 2 Marble colour logic
            when(player2Profile.marbleColour)
            {
                Marble.RED_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.red_baseline_circle_24)
                Marble.ORANGE_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.orange_baseline_circle_24)
                Marble.YELLOW_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.yellow_baseline_circle_24)
                Marble.GREEN_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.green_baseline_circle_24)
                Marble.BLUE_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.blue_baseline_circle_24)
                Marble.PURPLE_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.purple_baseline_circle_24)
                Marble.PINK_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.pink_baseline_circle_24)
                Marble.BLACK_MARBLE -> player2MarbleColourImageView.setImageResource(R.drawable.black_baseline_circle_24)
                else -> throw IllegalArgumentException("There is no valid image resource for Player 2 marble colour ${player1Profile.marbleColour}")
            }
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
            val players = PentagoRepository.get().getPlayerProfiles()
            player1Profile = players.get(0) //With the current implementation, player 1 will always be the user with lowest playerId (incase the app ever gets extended to allow multiple users
            player2Profile = players.get(1) //Player 2 will always be the second lowest playerId
        }
    }
}