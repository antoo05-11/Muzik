package com.example.muzik.ui.artist_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.muzik.R
import com.example.muzik.adapter.ListAlbumsVerticalPreviewAdapter
import com.example.muzik.adapter.ListSongsPreviewAdapter
import com.example.muzik.databinding.FragmentArtistBinding
import com.example.muzik.response_model.Album
import com.example.muzik.response_model.Song
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.utils.addDecorationForVerticalRcv
import com.example.muzik.utils.addSampleForRcv
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ArtistFragment : Fragment() {
    private lateinit var viewModel: ArtistViewModel
    private lateinit var binding: FragmentArtistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchArtistSongs(requireArguments().getInt("artistID"))
            viewModel.fetchArtistAlbums(requireArguments().getInt("artistID"))
        }
        Picasso.get().load(requireArguments().getString("artistImageURL"))
            .into(binding.artistImageView)
        binding.artistNameTextview.text = requireArguments().getString("artistName")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ArtistViewModel::class.java]
        val playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        val mainFragmentNavHostFragment = parentFragment as NavHostFragment
        val mainFragmentNavController = mainFragmentNavHostFragment.navController

        binding.backButton.setOnClickListener {
            mainFragmentNavController.popBackStack()
        }

        binding.rcvArtistPopularSongs.isNestedScrollingEnabled = false
        addDecorationForVerticalRcv(
            listOf(
                binding.rcvArtistPopularSongs,
                binding.rcvArtistPopularAlbums
            ), requireActivity()
        )
        addSampleForRcv(
            binding.rcvArtistPopularSongs,
            ListSongsPreviewAdapter::class.java,
            Song::class.java, 5
        )
        addSampleForRcv(
            binding.rcvArtistPopularAlbums,
            ListAlbumsVerticalPreviewAdapter::class.java,
            Album::class.java, 5, mainFragmentNavController as NavHostController
        )

        viewModel.artistSongs.observe(viewLifecycleOwner) {
            val adapter = ListSongsPreviewAdapter(it).hasItemIndexTextView().setFragmentOwner(this)
                .hasViewsShowed().setPlayerViewModel(playerViewModel)
            for (song in it) song.artistName = requireArguments().getString("artistName")
            binding.rcvArtistPopularSongs.adapter = adapter
        }

        viewModel.artistAlbums.observe(viewLifecycleOwner) {
            val adapter = ListAlbumsVerticalPreviewAdapter(
                it,
                requireParentFragment().childFragmentManager.findFragmentById(R.id.fragment_lib_nav)
                    ?.findNavController() as NavHostController
            )
            binding.rcvArtistPopularAlbums.adapter = adapter
        }

        return binding.root
    }
}