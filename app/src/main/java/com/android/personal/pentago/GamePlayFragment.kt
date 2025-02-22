package com.android.personal.pentago

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Visibility
import com.android.personal.pentago.databinding.FragmentGamePlayBinding
import com.android.personal.pentago.model.Marble
import com.android.personal.pentago.model.PentagoBoard
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class GamePlayFragment : Fragment()
{
    private var _binding: FragmentGamePlayBinding? = null
    private val binding: FragmentGamePlayBinding
        get() = checkNotNull(_binding) { "The FragmentGameplayBinding instance could not be accessed because it is currently null." }
    private val arguments: GamePlayFragmentArgs by navArgs()
    private lateinit var pentagoGameBoard: PentagoBoard
    private lateinit var uiPentagoBoard: Array<Array<ImageView?>>
    private lateinit var player1Profile: PlayerProfile
    private lateinit var player2Profile: PlayerProfile

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        _binding = FragmentGamePlayBinding.inflate(inflater, container, false)
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
                    val players = PentagoRepository.get().getPlayerProfiles()

                    player1Profile = players.get(0)

                    if(arguments.isAgainstAiOpponent)
                    {
                        player2Profile = players.get(2) //If the match is against the Ai opponent, set player 2 to the AI profile
                    }
                    else
                    {
                        player2Profile = players.get(1) //Else use the second human player profile
                    }
                    pentagoGameBoard = PentagoBoard(player1Profile, player2Profile, arguments.isAgainstAiOpponent)
                }
            }
            withContext(Dispatchers.Main)
            {
                binding.apply()
                {
                    player1MatchName.text = player1Profile.userName
                    player2MatchName.text = player2Profile.userName
                    playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName + "'s Turn"
                    gameStateHelpTextview.text = getText(R.string.game_state_help_place_marble)

                    activateGridCellClickListeners()

                    //Player 1 Profile Picture logic
                    when(player1Profile.profilePicture)
                    {
                        PlayerProfile.ANDROID_ROBOT_PP -> player1MatchIcon.setImageResource(R.drawable.android_robot_profile_picture)
                        PlayerProfile.BEACH_PP -> player1MatchIcon.setImageResource(R.drawable.beach_profile_picture)
                        PlayerProfile.DEFAULT_PP -> player1MatchIcon.setImageResource(R.drawable.default_avatar)
                        PlayerProfile.DESERT_PP -> player1MatchIcon.setImageResource(R.drawable.desert_profile_picture)
                        PlayerProfile.GIRAFFE_PP -> player1MatchIcon.setImageResource(R.drawable.giraffe_profile_picture)
                        PlayerProfile.LION_PP -> player1MatchIcon.setImageResource(R.drawable.lion_profile_picture)
                        PlayerProfile.MOUNTAIN_PP -> player1MatchIcon.setImageResource(R.drawable.mountain_profile_picture)
                        PlayerProfile.OSTRICH_PP -> player1MatchIcon.setImageResource(R.drawable.ostrich_profile_picture)
                        PlayerProfile.TIGER_PP -> player1MatchIcon.setImageResource(R.drawable.tiger_profile_picture)
                        PlayerProfile.TREE_PP -> player1MatchIcon.setImageResource(R.drawable.tree_profile_picture)
                        PlayerProfile.ZEBRA_PP -> player1MatchIcon.setImageResource(R.drawable.zebra_profile_picture)
                        else -> throw IllegalArgumentException("There is no valid image resource for Player 1 profile picture ${player1Profile.profilePicture}")
                    }

                    //Player 2 profile picture logic
                    when(player2Profile.profilePicture)
                    {
                        PlayerProfile.ANDROID_ROBOT_PP -> player2MatchIcon.setImageResource(R.drawable.android_robot_profile_picture)
                        PlayerProfile.BEACH_PP -> player2MatchIcon.setImageResource(R.drawable.beach_profile_picture)
                        PlayerProfile.DEFAULT_PP -> player2MatchIcon.setImageResource(R.drawable.default_avatar)
                        PlayerProfile.DESERT_PP -> player2MatchIcon.setImageResource(R.drawable.desert_profile_picture)
                        PlayerProfile.GIRAFFE_PP -> player2MatchIcon.setImageResource(R.drawable.giraffe_profile_picture)
                        PlayerProfile.LION_PP -> player2MatchIcon.setImageResource(R.drawable.lion_profile_picture)
                        PlayerProfile.MOUNTAIN_PP -> player2MatchIcon.setImageResource(R.drawable.mountain_profile_picture)
                        PlayerProfile.OSTRICH_PP -> player2MatchIcon.setImageResource(R.drawable.ostrich_profile_picture)
                        PlayerProfile.TIGER_PP -> player2MatchIcon.setImageResource(R.drawable.tiger_profile_picture)
                        PlayerProfile.TREE_PP -> player2MatchIcon.setImageResource(R.drawable.tree_profile_picture)
                        PlayerProfile.ZEBRA_PP -> player2MatchIcon.setImageResource(R.drawable.zebra_profile_picture)
                        PlayerProfile.AI_ROBOT_PP -> player2MatchIcon.setImageResource(R.drawable.ai_profile_picture)
                        else -> throw IllegalArgumentException("There is no valid image resource for Player 2 profile picture ${player2Profile.profilePicture}")
                    }

                    exitButton.setOnClickListener()
                    {
                        findNavController().navigate(GamePlayFragmentDirections.actionGamePlayFragmentToMainMenuFragment())
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

    private fun initialiseUIGameBoard()
    {
        uiPentagoBoard = Array<Array<ImageView?>>(6) { Array<ImageView?>(6) { null } }
        val numChildViews = 9
        var currentChildIndex = 0

        binding.apply()
        {
            //Adding the upper left subgrid image views
            for(i in 0..2)
            {
                for(j in 0..2)
                {
                    uiPentagoBoard[i][j] = pentagoUpperLeftSubgrid.getChildAt()
                }
            }
        }
    }

    private fun activateGridCellClickListeners()
    {
        activateUpperLeftSubgridCellListeners()
        activateUpperRightSubgridCellListeners()
        activateLowerLeftSubgridCellListeners()
        activateLowerRightSubgridCellListeners()
    }

    private fun activateUpperLeftSubgridCellListeners()
    {
        binding.apply()
        {
            //Upper left subgrid listeners
            (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(0, 0)
                    updateTopLeftCells(0, 0)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }

            (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(0, 1)
                    updateTopMiddleCells(0, 1)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(0, 2)
                    updateTopRightCells(0, 2)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(1, 0)
                    updateMiddleLeftCells(1, 0)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(1, 1)
                    updateMiddleMiddleCells(1, 1)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(1, 2)
                    updateMiddleRightCells(1, 2)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(2, 0)
                    updateBottomLeftCells(2, 0)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(2, 1)
                    updateBottomMiddleCells(2, 1)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(2, 2)
                    updateBottomRightCells(2, 2)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun activateUpperRightSubgridCellListeners()
    {
        binding.apply()
        {
            //Upper left subgrid listeners
            (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(0, 3)
                    updateTopLeftCells(0, 3)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }

            (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(0, 4)
                    updateTopMiddleCells(0, 4)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(0, 5)
                    updateTopRightCells(0, 5)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(1, 3)
                    updateMiddleLeftCells(1, 3)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(1, 4)
                    updateMiddleMiddleCells(1, 4)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(1, 5)
                    updateMiddleRightCells(1, 5)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(2, 3)
                    updateBottomLeftCells(2, 3)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(2, 4)
                    updateBottomMiddleCells(2, 4)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(2, 5)
                    updateBottomRightCells(2, 5)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun activateLowerLeftSubgridCellListeners()
    {
        binding.apply()
        {
            //Upper left subgrid listeners
            (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(3, 0)
                    updateTopLeftCells(3, 0)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }

            (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(3, 1)
                    updateTopMiddleCells(3, 1)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(3, 2)
                    updateTopRightCells(3, 2)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(4, 0)
                    updateMiddleLeftCells(4, 0)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(4, 1)
                    updateMiddleMiddleCells(4, 1)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(4, 2)
                    updateMiddleRightCells(4, 2)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(5, 0)
                    updateBottomLeftCells(5, 0)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(5, 1)
                    updateBottomMiddleCells(5, 1)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(5, 2)
                    updateBottomRightCells(5, 2)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun activateLowerRightSubgridCellListeners()
    {
        binding.apply()
        {
            //Upper left subgrid listeners
            (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(3, 3)
                    updateTopLeftCells(3, 3)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }

            (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(3, 4)
                    updateTopMiddleCells(3, 4)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(3, 5)
                    updateTopRightCells(3, 5)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(4, 3)
                    updateMiddleLeftCells(4, 3)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(4, 4)
                    updateMiddleMiddleCells(4, 4)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(4, 5)
                    updateMiddleRightCells(4, 5)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(5, 3)
                    updateBottomLeftCells(5, 3)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(5, 4)
                    updateBottomMiddleCells(5, 4)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
            (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setOnClickListener()
            {
                try
                {
                    pentagoGameBoard.placeMarble(5, 5)
                    updateBottomRightCells(5, 5)
                    gameStateHelpTextview.text = getString(R.string.game_state_help_rotate_subgrid)
                    activateWholeSubgridClickListeners()
                } catch(e: IllegalArgumentException)
                {
                    Toast.makeText(context, "There is already a marble here.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Changes the all grid cell clicklisteners to rotate the grid. I applied it to all cells because applying it to the subgrids themselves wasnt working
    private fun activateWholeSubgridClickListeners()
    {
        binding.apply()
        {
            for(i in 0..8)
            {
                pentagoUpperLeftSubgrid.getChildAt(i).setOnClickListener()
                {
                    rotateLinearLayout.visibility = View.VISIBLE
                    gameStateHelpTextview.text = getString(R.string.game_state_help_cw_aw)
                    //pentagoUpperLeftSubgrid.foreground = ResourcesCompat.getDrawable(R.drawable.)
                    clockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.TOP_LEFT_SUBGRID, PentagoBoard.CLOCKWISE_ROTATION)
                        rotateSubgridAnimation(pentagoUpperLeftSubgrid, PentagoBoard.CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                    anticlockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.TOP_LEFT_SUBGRID, PentagoBoard.ANTI_CLOCKWISE_ROTATION)
                        rotateSubgridAnimation(pentagoUpperLeftSubgrid, PentagoBoard.ANTI_CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                }
                pentagoUpperRightSubgrid.getChildAt(i).setOnClickListener()
                {
                    rotateLinearLayout.visibility = View.VISIBLE
                    gameStateHelpTextview.text = getString(R.string.game_state_help_cw_aw)
                    //pentagoUpperLeftSubgrid.foreground = ResourcesCompat.getDrawable(R.drawable.)
                    clockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.TOP_RIGHT_SUBGRID, PentagoBoard.CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                    anticlockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.TOP_RIGHT_SUBGRID, PentagoBoard.ANTI_CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                }
                pentagoLowerLeftSubgrid.getChildAt(i).setOnClickListener()
                {
                    rotateLinearLayout.visibility = View.VISIBLE
                    gameStateHelpTextview.text = getString(R.string.game_state_help_cw_aw)
                    //pentagoUpperLeftSubgrid.foreground = ResourcesCompat.getDrawable(R.drawable.)
                    clockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.BOTTOM_LEFT_SUBGRID, PentagoBoard.CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                    anticlockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.BOTTOM_LEFT_SUBGRID, PentagoBoard.ANTI_CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                }
                pentagoLowerRightSubgrid.getChildAt(i).setOnClickListener()
                {
                    rotateLinearLayout.visibility = View.VISIBLE
                    gameStateHelpTextview.text = getString(R.string.game_state_help_cw_aw)
                    //pentagoUpperLeftSubgrid.foreground = ResourcesCompat.getDrawable(R.drawable.)
                    clockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.BOTTOM_RIGHT_SUBGRID, PentagoBoard.CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                    anticlockwiseImageview.setOnClickListener()
                    {
                        pentagoGameBoard.rotateSubGrid(PentagoBoard.BOTTOM_RIGHT_SUBGRID, PentagoBoard.ANTI_CLOCKWISE_ROTATION)
                        updateUiGrids()
                        rotateLinearLayout.visibility = View.GONE
                        gameStateHelpTextview.text = getString(R.string.game_state_help_place_marble)
                        playerTurnTextview.text = pentagoGameBoard.currentTurnPlayerProfile.userName
                        activateGridCellClickListeners()
                    }
                }
            }
        }
    }


    private fun updateUiGrids()
    {
        for(i in 0..5)
        {
            for(j in 0..5)
            {
                //The functions are enclosed in if statements even though they dont need to be so that there is no processing power being wasted to check the conditionals within the functions
                if(pentagoGameBoard.pentagoBoard[i][j] != null) //Cells that have marbles
                {
                    if((i == 0 && j == 0) || (i == 0 && j == 3) || (i == 3 && j == 0) || (i == 3 && j == 3))
                    {
                        updateTopLeftCells(i, j)
                    }
                    if((i == 0 && j == 1) || (i == 0 && j == 4) || (i == 3 && j == 1) || (i == 3 && j == 4))
                    {
                        updateTopMiddleCells(i, j)
                    }
                    if((i == 0 && j == 2) || (i == 0 && j == 5) || (i == 3 && j == 2) || (i == 3 && j == 5))
                    {
                        updateTopRightCells(i, j)
                    }
                    if((i == 1 && j == 0) || (i == 1 && j == 3) || (i == 4 && j == 0) || (i == 4 && j == 3))
                    {
                        updateMiddleLeftCells(i, j)
                    }
                    if((i == 1 && j == 1) || (i == 1 && j == 4) || (i == 4 && j == 1) || (i == 4 && j == 4))
                    {
                        updateMiddleMiddleCells(i, j)
                    }
                    if((i == 1 && j == 2) || (i == 1 && j == 5) || (i == 4 && j == 2) || (i == 4 && j == 5))
                    {
                        updateMiddleRightCells(i, j)
                    }
                    if((i == 2 && j == 0) || (i == 2 && j == 3) || (i == 5 && j == 0) || (i == 5 && j == 3))
                    {
                        updateBottomLeftCells(i, j)
                    }
                    if((i == 2 && j == 1) || (i == 2 && j == 4) || (i == 5 && j == 1) || (i == 5 && j == 4))
                    {
                        updateBottomMiddleCells(i, j)
                    }
                    if((i == 2 && j == 2) || (i == 2 && j == 5) || (i == 5 && j == 2) || (i == 5 && j == 5))
                    {
                        updateBottomRightCells(i, j)
                    }

                }
                else //Cells without a marble
                {
                    updateEmptyCells(i, j)
                }
            }

        }
    }

    private fun updateEmptyCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(rowIndex)
            {
                0 ->
                {
                    when(columnIndex)
                    {
                        0 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.empty_upper_left_cell)
                        }

                        1 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        2 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.empty_upper_right_cell)
                        }

                        3 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.empty_upper_left_cell)
                        }

                        4 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        5 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.empty_upper_right_cell)
                        }
                    }
                }

                1 ->
                {
                    when(columnIndex)
                    {
                        0 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        1 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        2 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        3 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        4 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        5 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }
                    }
                }

                2 ->
                {
                    when(columnIndex)
                    {
                        0 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.empty_lower_left_cell)
                        }

                        1 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        2 ->
                        {
                            (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.empty_lower_right_cell)
                        }

                        3 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.empty_lower_left_cell)
                        }

                        4 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        5 ->
                        {
                            (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.empty_lower_right_cell)
                        }
                    }
                }

                3 ->
                {
                    when(columnIndex)
                    {
                        0 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.empty_upper_left_cell)
                        }

                        1 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        2 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.empty_upper_right_cell)
                        }

                        3 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.empty_upper_left_cell)
                        }

                        4 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        5 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.empty_upper_right_cell)
                        }
                    }
                }

                4 ->
                {
                    when(columnIndex)
                    {
                        0 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        1 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        2 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        3 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        4 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        5 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }
                    }
                }

                5 ->
                {
                    when(columnIndex)
                    {
                        0 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.empty_lower_left_cell)
                        }

                        1 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        2 ->
                        {
                            (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.empty_lower_right_cell)
                        }

                        3 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.empty_lower_left_cell)
                        }

                        4 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.empty_general_cell)
                        }

                        5 ->
                        {
                            (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.empty_lower_right_cell)
                        }
                    }
                }
            }
        }
    }

    private fun updateTopLeftCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.red_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.red_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.red_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.red_upper_left_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.orange_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.orange_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.orange_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.orange_upper_left_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.yellow_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.yellow_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.yellow_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.yellow_upper_left_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.green_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.green_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.green_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.green_upper_left_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.blue_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.blue_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.blue_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.blue_upper_left_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.purple_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.purple_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.purple_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.purple_upper_left_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.pink_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.pink_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.pink_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.pink_upper_left_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.black_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.black_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.black_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.black_upper_left_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.metallic_upper_left_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.metallic_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.metallic_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(0) as ImageView).setImageResource(R.drawable.metallic_upper_left_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateTopRightCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.red_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.red_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.red_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.red_upper_right_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.orange_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.orange_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.orange_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.orange_upper_right_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.yellow_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.yellow_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.yellow_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.yellow_upper_right_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.green_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.green_upper_left_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.green_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.green_upper_right_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.blue_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.blue_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.blue_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.blue_upper_right_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.purple_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.purple_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.purple_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.purple_upper_right_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.pink_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.pink_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.pink_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.pink_upper_right_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.black_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.black_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.black_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.black_upper_right_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.metallic_upper_right_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.metallic_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.metallic_upper_right_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(2) as ImageView).setImageResource(R.drawable.metallic_upper_right_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateBottomLeftCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.red_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.red_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.red_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.red_lower_left_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.orange_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.orange_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.orange_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.orange_lower_left_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.yellow_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.yellow_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.yellow_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.yellow_lower_left_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.green_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.green_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.green_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.green_lower_left_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.blue_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.blue_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.blue_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.blue_lower_left_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.purple_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.purple_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.purple_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.purple_lower_left_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.pink_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.pink_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.pink_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.pink_lower_left_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.black_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.black_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.black_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.black_lower_left_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.metallic_lower_left_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.metallic_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.metallic_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(6) as ImageView).setImageResource(R.drawable.metallic_lower_left_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateBottomRightCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.red_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.red_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.red_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.red_lower_right_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.orange_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.orange_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.orange_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.orange_lower_right_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.yellow_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.yellow_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.yellow_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.yellow_lower_right_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.green_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.green_lower_left_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.green_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.green_lower_right_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.blue_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.blue_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.blue_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.blue_lower_right_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.purple_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.purple_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.purple_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.purple_lower_right_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.pink_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.pink_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.pink_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.pink_lower_right_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.black_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.black_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.black_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.black_lower_right_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.metallic_lower_right_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.metallic_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.metallic_lower_right_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(8) as ImageView).setImageResource(R.drawable.metallic_lower_right_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateTopMiddleCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 0 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 0 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 3 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(1) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateMiddleLeftCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 0)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 3)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 0)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 3)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(3) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateMiddleMiddleCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(4) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateMiddleRightCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 1 && columnIndex == 2)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 1 && columnIndex == 5)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 2)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 4 && columnIndex == 5)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(5) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun updateBottomMiddleCells(rowIndex: Int, columnIndex: Int)
    {
        binding.apply()
        {
            when(checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour)
            {
                Marble.RED_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.red_general_cell)
                    }
                }

                Marble.ORANGE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.orange_general_cell)
                    }
                }

                Marble.YELLOW_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.yellow_general_cell)
                    }
                }

                Marble.GREEN_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.green_general_cell)
                    }
                }

                Marble.BLUE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.blue_general_cell)
                    }
                }

                Marble.PURPLE_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.purple_general_cell)
                    }
                }

                Marble.PINK_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.pink_general_cell)
                    }
                }

                Marble.BLACK_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.black_general_cell)
                    }
                }

                Marble.METALLIC_MARBLE ->
                {
                    if(rowIndex == 2 && columnIndex == 1)
                    {
                        (pentagoUpperLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 2 && columnIndex == 4)
                    {
                        (pentagoUpperRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 1)
                    {
                        (pentagoLowerLeftSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                    if(rowIndex == 5 && columnIndex == 4)
                    {
                        (pentagoLowerRightSubgrid.getChildAt(7) as ImageView).setImageResource(R.drawable.metallic_general_cell)
                    }
                }

                else -> throw IllegalArgumentException("There is no valid image resource for the marble colour present in the Pentago Board marble slot '${checkNotNull(pentagoGameBoard.pentagoBoard[rowIndex][columnIndex]).marbleColour}' at postion ${rowIndex}, ${columnIndex}")
            }
        }
    }

    private fun rotateSubgridAnimation(subgrid: GridLayout, rotationString: String)
    {
        var subgridAnimator: ObjectAnimator? = null

        if(rotationString == PentagoBoard.CLOCKWISE_ROTATION)
        {
            subgridAnimator = ObjectAnimator.ofFloat(subgrid, "rotation", 0f, 90f)
            subgridAnimator.setDuration(rotationAnimationDuration)
        }
        else
        {
            if(rotationString == PentagoBoard.ANTI_CLOCKWISE_ROTATION)
            {
                subgridAnimator = ObjectAnimator.ofFloat(subgrid, "rotation", 0f, -90f)
                subgridAnimator.setDuration(rotationAnimationDuration)
            }
        }
        checkNotNull(subgridAnimator).start()
    }

    //Pass the rotationString yo want to undo. E.g if you want to undo a clockwise rotation pass the clockwise string
    private fun instantUndoRotationAnimation(subgrid: GridLayout, rotationString: String)
    {
        var subgridAnimator: ObjectAnimator? = null

        if(rotationString == PentagoBoard.CLOCKWISE_ROTATION)
        {
            subgridAnimator = ObjectAnimator.ofFloat(subgrid, "rotation", 0f, -90f)
            subgridAnimator.setDuration(0)
        }
        else
        {
            if(rotationString == PentagoBoard.ANTI_CLOCKWISE_ROTATION)
            {
                subgridAnimator = ObjectAnimator.ofFloat(subgrid, "rotation", 0f, 90f)
                subgridAnimator.setDuration(0)
            }
        }
        checkNotNull(subgridAnimator).start()
    }

    companion object
    {
        const val rotationAnimationDuration: Long = 2000
    }
}