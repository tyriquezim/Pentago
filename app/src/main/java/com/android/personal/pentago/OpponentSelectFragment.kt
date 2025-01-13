package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.personal.pentago.databinding.FragmentOpponentSelectBinding

class OpponentSelectFragment : Fragment()
{
    private var _binding: FragmentOpponentSelectBinding? = null //Nullable backing property
    private val binding: FragmentOpponentSelectBinding
        get() = checkNotNull(_binding) {"The FragmentOpponentSelectBinding instance could not be accessed because it is currently null."}

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentOpponentSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //This is where the onClickListeners for the 2 buttons are set for screen navigation
        binding.apply()
        {
            humanOpponentButton.setOnClickListener()
            {
                findNavController().navigate(OpponentSelectFragmentDirections.actionOpponentSelectFragmentToGamePlayFragment())
            }
            aiOpponentButton.setOnClickListener()
            {
                findNavController().navigate(OpponentSelectFragmentDirections.actionOpponentSelectFragmentToGamePlayFragment())
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