package com.android.personal.pentago

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.personal.pentago.databinding.FragmentAchievementListBinding
import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class AchievementListFragment : Fragment()
{
    private var _binding: FragmentAchievementListBinding? = null
    private val binding: FragmentAchievementListBinding
        get() = checkNotNull(_binding) { "The FragmentAchievementListBinding instance could not be accessed because it is currently null." }
    private val arguments: AchievementListFragmentArgs by navArgs()
    private lateinit var playerProfile: PlayerProfile

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentAchievementListBinding.inflate(inflater, container, false)
        binding.achievementRecyclerView.layoutManager = LinearLayoutManager(context) //Gave it a linear layout manager because it determines how items will be positioned

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch()
        {
            lateinit var playerAchievements: List<Achievement>

            withContext(Dispatchers.IO)
            {
                PentagoRepository.get().mutex.withLock()
                {
                    playerProfile = PentagoRepository.get().getPlayerProfile(arguments.playerProfileId)
                    playerAchievements = playerProfile.getAchievements()
                }
            }
            withContext(Dispatchers.Main)
            {
                Log.d("Num Achievements", "${playerAchievements.size}")

                binding.backButton.setOnClickListener()
                {
                    findNavController().popBackStack()
                }
            }
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                binding.achievementRecyclerView.adapter = AchievementListAdapter(playerAchievements, ::onAchievementClicked)
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }

    fun onAchievementClicked(achievement: Achievement)
    {
        if(achievement.hasBeenEarned)
        {
            Toast.makeText(context, "You Earned ${achievement.achievementTitle}.\nCongratulations!!!", Toast.LENGTH_LONG).show()
        }
    }
}