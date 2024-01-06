package com.example.muzik.ui.fragment.explore_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.adapter.albums.AlbumsAdapterHorizontal
import com.example.muzik.adapter.artists.ArtistsAdapterHorizontal
import com.example.muzik.adapter.playlists.PlaylistsAdapterHorizontal
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentExploreBinding
import com.example.muzik.ui.fragment.main_fragment.MainFragment
import com.example.muzik.ui.fragment.player_view_fragment.PlayerViewModel
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

        addSampleForRcv(
            binding.rcvPlaylistsPreview,
            PlaylistsAdapterHorizontal::class.java,
            Playlist::class.java, 5
        )

        addSampleForRcv(
            binding.rcvListenAgainPlaylistsPreview,
            PlaylistsAdapterHorizontal::class.java,
            Playlist::class.java, 5
        )
        addSampleForRcv(
            binding.rcvYourArtistPreview,
            ArtistsAdapterHorizontal::class.java,
            Artist::class.java, 5
        )
        addSampleForRcv(
            binding.rcvRecentAlbumsPreview,
            AlbumsAdapterHorizontal::class.java,
            Album::class.java, 5
        )
        addSampleForRcv(
            binding.rcvForYouSongsPreview,
            SongsAdapterVertical::class.java,
            Song::class.java, 5
        )

        val mainFragment = requireParentFragment().parentFragment as MainFragment

        viewModel.topPlaylistsList.observe(viewLifecycleOwner) {
            val adapter = PlaylistsAdapterHorizontal(it)
            adapter.setObjectAction(mainFragment)
            binding.rcvPlaylistsPreview.adapter = adapter
        }
        viewModel.topSongList.observe(viewLifecycleOwner) {

            val adapter = SongsAdapterVertical(it.subList(0, 5)).setFragmentOwner(this)
                .setPlayerViewModel(playerViewModel).setObjectAction(mainFragment)
            binding.rcvForYouSongsPreview.adapter = adapter
        }
        viewModel.listenAgainPlaylistsList.observe(viewLifecycleOwner) {
            val adapter = PlaylistsAdapterHorizontal(it)
            adapter.setObjectAction(mainFragment)
            binding.rcvListenAgainPlaylistsPreview.adapter = adapter
        }
        viewModel.recentAlbumsList.observe(viewLifecycleOwner) {
            val adapter = AlbumsAdapterHorizontal(it).setObjectAction(mainFragment)
            binding.rcvRecentAlbumsPreview.adapter = adapter
        }
        viewModel.yourArtistsList.observe(viewLifecycleOwner) {
            val adapter = ArtistsAdapterHorizontal(it).setObjectAction(mainFragment)
            binding.rcvYourArtistPreview.adapter = adapter
        }

        return binding.root
    }
}