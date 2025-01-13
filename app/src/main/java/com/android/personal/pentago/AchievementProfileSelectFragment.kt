package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.personal.pentago.databinding.FragmentAchievementProfileSelectBinding

class AchievementProfileSelectFragment : Fragment()
{
    private var _binding: FragmentAchievementProfileSelectBinding? = null
    private val binding: FragmentAchievementProfileSelectBinding
        get() = checkNotNull(_binding) {"The FragmentAchievementProfileSelectBinding instance could not be accessed because it is currently null."}

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentAchievementProfileSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.apply()
        {
            player1Button.setOnClickListener()
            {
                findNavController().navigate(AchievementProfileSelectFragmentDirections.actionAchievementProfileSelectFragmentToAchievementListFragment2())
            }
            player2Button.setOnClickListener()
            {
                findNavController().navigate(AchievementProfileSelectFragmentDirections.actionAchievementProfileSelectFragmentToAchievementListFragment2())
            }
            backButton.setOnClickListener()
            {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }
}