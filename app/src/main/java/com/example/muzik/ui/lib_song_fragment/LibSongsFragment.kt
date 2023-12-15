package com.example.muzik.ui.lib_song_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.ListSongAdapter
import com.example.muzik.databinding.FragmentLibSongsBinding
import com.example.muzik.music_service.local.LocalMusicRepository
import com.example.muzik.ui.player_view_fragment.PlayerViewModel

class LibSongsFragment: Fragment() {
    private lateinit var binding: FragmentLibSongsBinding

    private lateinit var songViewModel: SongViewModel
    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var listSongAdapter: ListSongAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibSongsBinding.inflate(inflater)

        songViewModel = ViewModelProvider(requireActivity())[SongViewModel::class.java]
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        listSongAdapter = ListSongAdapter(LocalMusicRepository.getSongs(), playerViewModel)
        binding.rvListSong.adapter = listSongAdapter
        binding.rvListSong.layoutManager = LinearLayoutManager(this.context)


        return binding.root
    }
}