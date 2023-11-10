package com.example.muzik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentPlayerViewBinding
import com.example.muzik.utils.getReadableTime
import com.example.muzik.viewmodel.PlayerViewModel

class PlayerViewFragment: Fragment() {

    private lateinit var binding: FragmentPlayerViewBinding

    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerViewBinding.inflate(inflater)
        playerViewModel = ViewModelProvider(requireActivity()).get(PlayerViewModel::class.java)
        binding.tvPlayPause.setOnClickListener {
            playerViewModel.playPause()
        }

        playerViewModel.playingMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvPlayPause.text = "pause"
            } else {
                binding.tvPlayPause.text = "play"
            }
        }

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.name
            binding.tvTotal.text = getReadableTime(it.duration)
            binding.sb.max = it.duration
        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            binding.sb.progress = it
            binding.tvCurrent.text = getReadableTime(it)
        }


        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_nav) as NavHostFragment
        val navController = navHostFragment.navController
//        val navController = findNavController()
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.libraryFragment)
        }

        binding.sb.setOnSeekBarChangeListener(PlayerViewModel.OnSeekBarChangeListener(playerViewModel))

        return binding.root
    }
}