package com.example.muzik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentPlayerViewBinding
import com.example.muzik.utils.getReadableTime
import com.example.muzik.viewmodel.PlayerViewModel

class PlayerViewFragment : Fragment() {

    private lateinit var binding: FragmentPlayerViewBinding

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var exoPlayer: ExoPlayer;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerViewBinding.inflate(inflater)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        binding.tvPlayPause.setOnClickListener {
            playerViewModel.playPause()
        }

        playerViewModel.playingMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                //binding.tvPlayPause.text = "pause"
            } else {
                //binding.tvPlayPause.text = "play"
            }
        }

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.name
            binding.tvTotal.text = getReadableTime(it.duration!!)
            //binding.sb.max = it.duration
        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            //binding.sb.progress = it
            binding.tvCurrent.text = getReadableTime(it)
        }


        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_nav) as NavHostFragment
        val navController = navHostFragment.navController
//        val navController = findNavController()
        binding.backBtn.setOnClickListener {
            navController.navigate(R.id.libraryFragment)
        }

        // binding.sb.setOnSeekBarChangeListener(PlayerViewModel.OnSeekBarChangeListener(playerViewModel))

        exoPlayer = ExoPlayer.Builder(this.requireActivity().applicationContext).build()
        binding.videoView.player = exoPlayer
        exoPlayer.setMediaItem(
            MediaItem.fromUri(
                "http:/10.0.2.2:6600/api/song/stream/20ebf406df941e30e6f7muzikUETK661ac0f940eed15ebe74c56a5a85758f91muzikUETK6617a991e333a6d57ec85e3de6eee584053a7b9044a5bf3e5e8e6be58f29807cb6.m3u8"
            )
        )

        exoPlayer.prepare()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.pause()
        exoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

}