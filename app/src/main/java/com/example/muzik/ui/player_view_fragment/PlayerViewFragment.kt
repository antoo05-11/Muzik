package com.example.muzik.ui.player_view_fragment

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentPlayerViewBinding
import com.example.muzik.utils.PaletteUtils
import com.example.muzik.utils.getReadableTime
import com.example.muzik.utils.setRotateAnimation
import com.squareup.picasso.Callback
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
        val anim = setRotateAnimation(binding.activityTrackImage)


        binding.playPauseSongButton.setOnClickListener {
            playerViewModel.playPause()
        }

        binding.nextSongButton.setOnClickListener {

        }

        binding.prevSongButton.setOnClickListener {

        }

        binding.loveSongButton.setOnClickListener {
            val currentBackground = binding.loveSongButton.background

            if (currentBackground.constantState == ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.icon_heart
                )?.constantState
            ) {
                binding.loveSongButton.setBackgroundResource(R.drawable.icon_full_heart)
            } else {
                binding.loveSongButton.setBackgroundResource(R.drawable.icon_heart)
            }
        }

        playerViewModel.playingMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.playPauseSongButton.setBackgroundResource(R.drawable.icon_pause)
                anim.resume()
            } else {
                binding.playPauseSongButton.setBackgroundResource(R.drawable.icon_play)
                anim.pause()
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
            if (it.imageURI == null) {
                binding.activityTrackImage.setBackgroundResource(R.drawable.icons8_song_500_1_)
            }
            Picasso.get()
                .load(it.imageURI)
                .into(binding.activityTrackImage, object : Callback {
                    override fun onSuccess() {
                        binding.root.background = PaletteUtils.getDominantGradient(
                            bitmap = (binding.activityTrackImage.drawable as BitmapDrawable).bitmap,
                            endColor = "#303030"
                        )
                    }

                    override fun onError(e: Exception?) {
                    }

                })

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

        // TODO: Navigate to artist fragment of main fragment.
        binding.tvArtistName.setOnClickListener {
            navController.navigate(R.id.libraryFragment)
        }

        binding.prevSongButton.setOnClickListener {
            playerViewModel.skipPreSong()
        }

        binding.nextSongButton.setOnClickListener {
            playerViewModel.skipNextSong()
        }

        binding.exoRepeatToggle.setOnClickListener {
            playerViewModel.switchRepeatMode()
        }

        playerViewModel.repeatModeMutableLiveData.observe(viewLifecycleOwner) {
            when (it) {
                Player.REPEAT_MODE_OFF -> binding.exoRepeatToggle.setBackgroundResource(R.drawable.baseline_repeat_24)
                Player.REPEAT_MODE_ONE -> binding.exoRepeatToggle.setBackgroundResource(R.drawable.baseline_repeat_one_24)
                Player.REPEAT_MODE_ALL -> binding.exoRepeatToggle.setBackgroundResource(R.drawable.baseline_repeat_on_24)
            }
        }

        binding.exoShuffle.setOnClickListener {
            playerViewModel.switchShuffleMode()
        }

        playerViewModel.shuffleModeMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.exoShuffle.setBackgroundResource(R.drawable.baseline_shuffle_on_24)
            } else {
                binding.exoShuffle.setBackgroundResource(R.drawable.baseline_shuffle_24)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

}