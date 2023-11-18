package com.example.muzik.ui.player_view_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentPlayerViewBinding
import com.example.muzik.utils.getReadableTime
import com.example.muzik.utils.setRotateAnimation
import com.squareup.picasso.Picasso


class PlayerViewFragment : Fragment() {

    private lateinit var binding: FragmentPlayerViewBinding

    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerViewBinding.inflate(inflater)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        setRotateAnimation(binding.activityTrackImage)

        binding.playPauseSongButton.setOnClickListener {
            playerViewModel.playPause()
        }

        binding.nextSongButton.setOnClickListener {

        }

        binding.prevSongButton.setOnClickListener {

        }

        playerViewModel.playingMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.playPauseSongButton.setBackgroundResource(R.drawable.ic_pause_circle)
            } else {
                binding.playPauseSongButton.setBackgroundResource(R.drawable.ic_play_circle)
            }
        }

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.name
            binding.tvTotal.text = getReadableTime(it.duration!!)
            binding.sb.max = it.duration
        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            binding.sb.progress = it
            binding.tvCurrent.text = getReadableTime(it)
        }

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            if (it.imageURL.isNotEmpty())
                Picasso.get()
                    .load(it.imageURL)
                    .fit()
                    .centerInside()
                    .into(binding.activityTrackImage)

            binding.tvArtistName.text = it.artistName
        }

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_nav) as NavHostFragment
        val navController = navHostFragment.navController

        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.libraryFragment)
        }

        binding.sb.setOnSeekBarChangeListener(
            PlayerViewModel.OnSeekBarChangeListener(
                playerViewModel
            )
        )


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

}