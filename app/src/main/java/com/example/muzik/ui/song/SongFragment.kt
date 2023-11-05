package com.example.muzik.ui.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.muzik.databinding.FragmentSongBinding
import com.squareup.picasso.Picasso

class SongFragment: Fragment() {

    private val songViewModel: SongViewModel by viewModels()

    private var _binding: FragmentSongBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup a click listener for the buttons.
        binding.exoPlay.setOnClickListener { onPlaySong() }
        binding.exoNext.setOnClickListener { onNextSong() }
        binding.exoPrev.setOnClickListener { onPrevSong() }
        binding.exoShuffle.setOnClickListener { onShuffleSong() }
        binding.keyboardDown.setOnClickListener { onKeyboardDown() }
        binding.keyboardDown.setOnClickListener { onSettingSong() }

        //update the UI
        updateScreen()

    }
    private fun onPlaySong() {

    }
    private fun onStopSong() {

    }
    private fun onNextSong() {

    }
    private fun onPrevSong() {

    }
    private fun onShuffleSong() {

    }
    private fun onKeyboardDown() {

    }
    private fun onSettingSong() {

    }
    private fun updateScreen() {
        binding.nameSong.text = songViewModel.name
        binding.artistName.text = songViewModel.artistName
        Picasso.get().load(songViewModel.imageURL).into(binding.activityTrackImage)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}