package com.example.muzik.ui.explore_fragment

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
import com.example.muzik.adapter.ListAlbumsHorizontalPreviewAdapter
import com.example.muzik.adapter.ListArtistsPreviewAdapter
import com.example.muzik.adapter.ListPlaylistsPreviewAdapter
import com.example.muzik.adapter.ListSongsPreviewAdapter
import com.example.muzik.databinding.FragmentExploreBinding
import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Artist
import com.example.muzik.response_model.Playlist
import com.example.muzik.response_model.Song
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.utils.addDecorationForHorizontalRcv
import com.example.muzik.utils.addDecorationForVerticalRcv
import com.example.muzik.utils.addSampleForRcv
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.fetchData(requireContext())
        }
    }

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
            mutableListOf(
                Triple(
                    binding.rcvPlaylistsPreview,
                    ListPlaylistsPreviewAdapter::class.java,
                    Playlist::class.java
                ),
                Triple(
                    binding.rcvListenAgainPlaylistsPreview,
                    ListPlaylistsPreviewAdapter::class.java,
                    Playlist::class.java
                )
            ),
            5
        )
        addSampleForRcv(
            binding.rcvYourArtistPreview,
            ListArtistsPreviewAdapter::class.java,
            Artist::class.java, 5, findNavController() as NavHostController
        )
        addSampleForRcv(
            binding.rcvRecentAlbumsPreview,
            ListAlbumsHorizontalPreviewAdapter::class.java,
            Album::class.java, 5, findNavController() as NavHostController
        )
        addSampleForRcv(
            binding.rcvForYouSongsPreview,
            ListSongsPreviewAdapter::class.java,
            Song::class.java, 5
        )

        // For rendering data.
        viewModel.topPlaylistsList.observe(viewLifecycleOwner) { it ->
            val adapter = ListPlaylistsPreviewAdapter(it);
            binding.rcvPlaylistsPreview.adapter = adapter
        }
        viewModel.topSongList.observe(viewLifecycleOwner) {
            val adapter = ListSongsPreviewAdapter(it)
            adapter.setPlayerViewModel(playerViewModel)
            binding.rcvForYouSongsPreview.adapter = adapter
        }
        viewModel.listenAgainPlaylistsList.observe(viewLifecycleOwner) {
            val adapter = ListPlaylistsPreviewAdapter(it)
            binding.rcvListenAgainPlaylistsPreview.adapter = adapter
        }
        viewModel.recentAlbumsList.observe(viewLifecycleOwner) {
            val adapter =
                ListAlbumsHorizontalPreviewAdapter(
                    it,
                    requireParentFragment().childFragmentManager.findFragmentById(R.id.fragment_lib_nav)
                        ?.findNavController() as NavHostController
                )
            binding.rcvRecentAlbumsPreview.adapter = adapter
        }
        viewModel.yourArtistsList.observe(viewLifecycleOwner) {
            val adapter = ListArtistsPreviewAdapter(
                it,
                requireParentFragment().childFragmentManager.findFragmentById(R.id.fragment_lib_nav)
                    ?.findNavController() as NavHostController
            )
            binding.rcvYourArtistPreview.adapter = adapter
        }

        return binding.root
    }
}