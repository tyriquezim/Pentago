package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.personal.pentago.databinding.FragmentMarbleColourSelectBinding
import com.android.personal.pentago.model.Marble
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class MarbleColourSelectFragment : Fragment()
{
    private var _binding: FragmentMarbleColourSelectBinding? = null
    private val binding: FragmentMarbleColourSelectBinding
        get() = checkNotNull(_binding) { "The FragmentMarbleColourSelectBinding instance could not be accessed because it is currently null." }
    private val arguments: MarbleColourSelectFragmentArgs by navArgs()
    private lateinit var playerProfile: PlayerProfile

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        _binding = FragmentMarbleColourSelectBinding.inflate(inflater, container, false)
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
                    redMarbleImageView.setOnClickListener()
                    {
                        if(Marble.RED_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.RED_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    orangeMarbleImageView.setOnClickListener()
                    {
                        if(Marble.ORANGE_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.ORANGE_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    yellowMarbleImageView.setOnClickListener()
                    {
                        if(Marble.YELLOW_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.YELLOW_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    greenMarbleImageView.setOnClickListener()
                    {
                        if(Marble.GREEN_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.GREEN_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    blueMarbleImageView.setOnClickListener()
                    {
                        if(Marble.BLUE_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.BLUE_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    purpleMarbleImageView.setOnClickListener()
                    {
                        if(Marble.PURPLE_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.PURPLE_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    pinkMarbleImageView.setOnClickListener()
                    {
                        if(Marble.PINK_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.PINK_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    blackMarbleImageView.setOnClickListener()
                    {
                        if(Marble.BLACK_MARBLE in PlayerProfile.activeMarbleColourSet)
                        {
                            Toast.makeText(context, "Cannot update marble colour! Marble colours must be unique among players.", Toast.LENGTH_LONG).show()
                        }
                        else
                        {
                            GlobalScope.launch()
                            {
                                withContext(Dispatchers.IO)
                                {
                                    PentagoRepository.get().mutex.withLock()
                                    {
                                        playerProfile.marbleColour = Marble.BLACK_MARBLE
                                        PentagoRepository.get().updatePlayerProfileMarbleColour(playerProfile.playerId, playerProfile.marbleColour)
                                    }
                                }
                            }
                            findNavController().popBackStack()
                        }
                    }
                    backButton.setOnClickListener()
                    {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}