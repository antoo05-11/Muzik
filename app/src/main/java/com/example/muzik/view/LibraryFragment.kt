package com.example.muzik.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.R
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.viewmodel.PlayerViewModel
import com.example.muzik.viewmodel.SongViewModel

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    private lateinit var songViewModel: SongViewModel

    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater)
        songViewModel = ViewModelProvider(requireActivity())[SongViewModel::class.java]
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
//        binding.rv.layoutManager = LinearLayoutManager(this.context)
//        songViewModel.songsMutableLiveData.observe(viewLifecycleOwner) {
//            val adapter = ListSongAdapter(it, playerViewModel)
//            binding.rv.adapter = adapter
//        }
        Log.d("test",
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_nav)
                .toString()
        )
        val mainNavHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_nav) as NavHostFragment
        val mainNavController = mainNavHostFragment.navController
//        val navController = findNavController()

        val libNavHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_lib_nav) as NavHostFragment
        val libNavController = libNavHostFragment.navController

        binding.tvAlbums.setOnClickListener {
            libNavController.navigate(R.id.libAlbumsFragment)
        }
        binding.tvSongs.setOnClickListener {
            libNavController.navigate(R.id.libSongsFragment)
        }
        binding.tvArtist.setOnClickListener {
            libNavController.navigate(R.id.libArtistFragment)
        }
        binding.tvPlaylists.setOnClickListener {
            libNavController.navigate(R.id.libPlaylistFragment)
        }

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            binding.tvSongNamePreview.text = it.name
            binding.pbSongPreview.max = it.duration!!

        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            binding.pbSongPreview.progress = it
        }

        playerViewModel.isSelectedMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.clPreview.visibility = View.VISIBLE
            }

        }

        binding.clPreview.setOnClickListener {
            mainNavController.navigate(R.id.playerViewFragment)
        }

        return binding.root
    }
}