package com.example.muzik.ui.library_fragment.lib_playlist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.playlists.PlaylistsAdapterVertical
import com.example.muzik.databinding.FragmentLibPlaylistBinding
import kotlinx.coroutines.launch

class LibPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentLibPlaylistBinding
    private lateinit var viewModel: PlaylistViewModel

    private lateinit var adapter: PlaylistsAdapterVertical

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibPlaylistBinding.inflate(inflater)
        viewModel = ViewModelProvider(requireActivity())[PlaylistViewModel::class.java]

        val navHostController =
            parentFragment?.parentFragment?.childFragmentManager?.findFragmentById(R.id.fragment_lib_nav)
                ?.findNavController() as NavHostController

        adapter = PlaylistsAdapterVertical(mutableListOf(), navHostController)
        binding.rcvPlaylists.adapter = adapter
        binding.rcvPlaylists.layoutManager = LinearLayoutManager(context)

        viewModel.playlists.observe(viewLifecycleOwner) {
            adapter.listPlaylist = it
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchData()
        }
    }
}