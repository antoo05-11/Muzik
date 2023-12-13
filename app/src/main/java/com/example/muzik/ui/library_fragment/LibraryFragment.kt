package com.example.muzik.ui.library_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.ListArtistAdapter
import com.example.muzik.adapter.ListPlaylistAdapter
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.response_model.Playlist
import com.example.muzik.utils.addDecorationForVerticalRcv

class LibraryFragment : Fragment() {

    private lateinit var viewModel: LibraryViewModel
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var rcvPlaylistList: RecyclerView
    private lateinit var rcvArtistList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        binding = FragmentLibraryBinding.inflate(inflater, container, false)

        rcvPlaylistList = binding.yourPlaylistsRcv
        rcvArtistList = binding.yourArtistsRcv

        activity?.let {
            addDecorationForVerticalRcv(rcvPlaylistList, it)
            addDecorationForVerticalRcv(rcvArtistList, it)
        }

        val navHostFragmentOwnerController =
            requireParentFragment().childFragmentManager.findFragmentById(R.id.fragment_lib_nav)
                ?.findNavController() as NavHostController

        viewModel.topPlaylistsList.observe(viewLifecycleOwner) {
            val playlists = it.toMutableList()
            playlists.add(0, Playlist(true))
            val adapter = ListPlaylistAdapter(playlists, navHostFragmentOwnerController)
            rcvPlaylistList.adapter = adapter
        }

        viewModel.yourArtistsList.observe(viewLifecycleOwner) {
            val adapter = ListArtistAdapter(it, navHostFragmentOwnerController)
            rcvArtistList.adapter = adapter
        }

        return binding.root
    }
}