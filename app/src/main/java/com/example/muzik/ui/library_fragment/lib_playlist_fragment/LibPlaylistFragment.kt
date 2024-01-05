package com.example.muzik.ui.library_fragment.lib_playlist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.playlists.PlaylistsAdapterVertical
import com.example.muzik.databinding.FragmentLibPlaylistBinding
import com.example.muzik.ui.main_activity.MainActivity
import com.example.muzik.ui.main_fragment.MainFragment

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
        binding.rcvPlaylists.layoutManager = LinearLayoutManager(context)

        adapter = PlaylistsAdapterVertical()
        adapter.setObjectAction(requireParentFragment().requireParentFragment().parentFragment as MainFragment)
        binding.rcvPlaylists.adapter = adapter

        MainActivity.userPlaylists.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        return binding.root
    }
}