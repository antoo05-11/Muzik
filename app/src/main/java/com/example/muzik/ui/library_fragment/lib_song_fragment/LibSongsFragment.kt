package com.example.muzik.ui.library_fragment.lib_song_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.databinding.FragmentLibSongsBinding
import com.example.muzik.music_service.LocalMusicRepository
import com.example.muzik.ui.player_view_fragment.PlayerViewModel

class LibSongsFragment : Fragment() {
    private lateinit var binding: FragmentLibSongsBinding

    private lateinit var songViewModel: SongViewModel
    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var listSongAdapter: SongsAdapterVertical

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibSongsBinding.inflate(inflater)

        songViewModel = ViewModelProvider(requireActivity())[SongViewModel::class.java]
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        listSongAdapter = SongsAdapterVertical(
            LocalMusicRepository.getSongs().toMutableList()
        ).setFragmentOwner(this).setPlayerViewModel(playerViewModel)
        binding.rvListSong.adapter = listSongAdapter
        binding.rvListSong.layoutManager = LinearLayoutManager(this.context)

        songViewModel.songs.observe(viewLifecycleOwner){
            listSongAdapter.updateList(it)
            listSongAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel.initSongs()
    }
}