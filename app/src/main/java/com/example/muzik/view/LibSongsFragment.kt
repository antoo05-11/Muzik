package com.example.muzik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.ListSongAdapter
import com.example.muzik.databinding.FragmentLibSongsBinding
import com.example.muzik.viewmodel.PlayerViewModel
import com.example.muzik.viewmodel.SongViewModel

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

        songViewModel.songsMutableLiveData.observe(viewLifecycleOwner){
            listSongAdapter = ListSongAdapter(it, playerViewModel)
            binding.rvListSong.adapter = listSongAdapter
            binding.rvListSong.layoutManager = LinearLayoutManager(this.context)
        }

        return binding.root
    }
}