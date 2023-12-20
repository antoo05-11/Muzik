package com.example.muzik.ui.explore_fragment

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
import com.example.muzik.R
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.adapter.albums.AlbumsAdapterHorizontal
import com.example.muzik.adapter.artists.ArtistsAdapterHorizontal
import com.example.muzik.adapter.playlists.PlaylistsAdapterHorizontal
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentExploreBinding
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.utils.addDecorationForHorizontalRcv
import com.example.muzik.utils.addDecorationForVerticalRcv
import com.example.muzik.utils.addSampleForRcv

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData(lifecycleScope)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[ExploreViewModel::class.java]
        val playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        // For entire view.
        addDecorationForHorizontalRcv(
            listOf(
                binding.rcvPlaylistsPreview,
                binding.rcvListenAgainPlaylistsPreview,
                binding.rcvYourArtistPreview,
                binding.rcvRecentAlbumsPreview
            ),
            requireActivity()
        )
        binding.rcvForYouSongsPreview.isNestedScrollingEnabled = false
        addDecorationForVerticalRcv(binding.rcvForYouSongsPreview, requireActivity())

        // For loading view.
        addSampleForRcv(
            binding.rcvPlaylistsPreview,
            PlaylistsAdapterHorizontal::class.java,
            Playlist::class.java, 5, findNavController() as NavHostController
        )

        addSampleForRcv(
            binding.rcvListenAgainPlaylistsPreview,
            PlaylistsAdapterHorizontal::class.java,
            Playlist::class.java, 5, findNavController() as NavHostController
        )
        addSampleForRcv(
            binding.rcvYourArtistPreview,
            ArtistsAdapterHorizontal::class.java,
            Artist::class.java, 5, findNavController() as NavHostController
        )
        addSampleForRcv(
            binding.rcvRecentAlbumsPreview,
            AlbumsAdapterHorizontal::class.java,
            Album::class.java, 5, findNavController() as NavHostController
        )
        addSampleForRcv(
            binding.rcvForYouSongsPreview,
            SongsAdapterVertical::class.java,
            Song::class.java, 5
        )

        val navHostFragmentOwnerController =
            requireParentFragment().childFragmentManager.findFragmentById(R.id.fragment_lib_nav)
                ?.findNavController() as NavHostController

        // For rendering data.
        viewModel.topPlaylistsList.observe(viewLifecycleOwner) {
            val adapter = PlaylistsAdapterHorizontal(
                it,
                navHostFragmentOwnerController
            );
            binding.rcvPlaylistsPreview.adapter = adapter
        }
        viewModel.topSongList.observe(viewLifecycleOwner) {
            val adapter = SongsAdapterVertical(it).setFragmentOwner(this)
                .setPlayerViewModel(playerViewModel)
            binding.rcvForYouSongsPreview.adapter = adapter
        }
        viewModel.listenAgainPlaylistsList.observe(viewLifecycleOwner) {
            val adapter = PlaylistsAdapterHorizontal(
                it,
                navHostFragmentOwnerController
            )
            binding.rcvListenAgainPlaylistsPreview.adapter = adapter
        }
        viewModel.recentAlbumsList.observe(viewLifecycleOwner) {
            val adapter =
                AlbumsAdapterHorizontal(
                    it,
                    navHostFragmentOwnerController
                )
            binding.rcvRecentAlbumsPreview.adapter = adapter
        }
        viewModel.yourArtistsList.observe(viewLifecycleOwner) {
            val adapter =
                ArtistsAdapterHorizontal(
                    it,
                    navHostFragmentOwnerController
                )
            binding.rcvYourArtistPreview.adapter = adapter
        }

        return binding.root
    }
}