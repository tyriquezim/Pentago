package com.android.personal.pentago

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.personal.pentago.databinding.FragmentHowToPlayBinding
import com.android.personal.pentago.databinding.FragmentStatsProfileSelectBinding

class HowToPlayFragment : Fragment()
{
    private var _binding: FragmentHowToPlayBinding? = null
    private val binding: FragmentHowToPlayBinding
        get() = checkNotNull(_binding) {"The FragmentHowToPlayBinding instance could not be accessed because it is currently null."} //Nullable backing property

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentHowToPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.apply()
        {
            backButton.setOnClickListener()
            {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding =  null //Removing the reference to the view to conserve memory
    }
}