package com.android.personal.pentago

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.personal.pentago.databinding.FragmentProfileAvatarSelectBinding
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ProfileAvatarSelectFragment : Fragment()
{
    private var _binding: FragmentProfileAvatarSelectBinding? = null
    private val binding: FragmentProfileAvatarSelectBinding
        get() = checkNotNull(_binding) { "The FragmentProfileAvatarSelectBinding instance could not be accessed because it is currently null." }
    private val arguments: ProfileAvatarSelectFragmentArgs by navArgs()
    private lateinit var playerProfile: PlayerProfile

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentProfileAvatarSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch()
        {
            withContext(Dispatchers.IO)
            {
                PentagoRepository.get().mutex.withLock()
                {
                    playerProfile = PentagoRepository.get().getPlayerProfile(arguments.playerProfileId)
                }
            }

            binding.apply()
            {
                withContext(Dispatchers.Main)
                {
                    defaultAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch() //Nested coroutine since i want the database to updated
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.DEFAULT_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    androidRobotAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.ANDROID_ROBOT_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    sunsetAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.TREE_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    coastlineAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.BEACH_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    mountainAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.MOUNTAIN_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    desertAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.DESERT_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    lionAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.LION_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    tigerAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.TIGER_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    giraffeAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.GIRAFFE_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    ostrichAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.OSTRICH_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
                    }
                    zebraAvatarImageView.setOnClickListener()
                    {
                        GlobalScope.launch()
                        {
                            withContext(Dispatchers.IO)
                            {
                                PentagoRepository.get().mutex.withLock()
                                {
                                    playerProfile.profilePicture = PlayerProfile.ZEBRA_PP
                                    PentagoRepository.get().updatePlayerProfileProfilePicture(playerProfile.playerId, playerProfile.profilePicture)
                                }
                            }
                        }
                        findNavController().popBackStack()
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