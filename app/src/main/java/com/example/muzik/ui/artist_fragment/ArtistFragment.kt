package com.example.muzik.ui.artist_fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.adapter.albums.AlbumsAdapterVertical
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentArtistBinding
import com.example.muzik.ui.main_fragment.MainFragment
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
            var artistID = requireArguments().getLong("artistID", -1)
            if (artistID == -1L) {
                artistID = (requireArguments().getSerializable("artist") as Artist).artistID
            }
            viewModel.fetchArtist(artistID)
            viewModel.fetchArtistSongs(artistID)
            viewModel.fetchArtistAlbums(artistID)
        }
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
            SongsAdapterVertical::class.java,
            Song::class.java, 5
        )
        addSampleForRcv(
            binding.rcvArtistPopularAlbums,
            AlbumsAdapterVertical::class.java,
            Album::class.java, 5
        )

        viewModel.artist.observe(viewLifecycleOwner) {
            Picasso.get().load(it.imageURI).into(binding.artistImageView)
            binding.artistNameTextview.text = it.name
            binding.artistNameTextview.setBackgroundColor(Color.parseColor("#1C444444"))
        }

        viewModel.artistSongs.observe(viewLifecycleOwner) {
            val adapter = SongsAdapterVertical(it).hasItemIndexTextView().setFragmentOwner(this)
                .hasViewsShowed().setPlayerViewModel(playerViewModel)
            for (song in it) song.artistName = (requireArguments().getString("artistName"))
            binding.rcvArtistPopularSongs.adapter = adapter
        }

        viewModel.artistAlbums.observe(viewLifecycleOwner) {
            val adapter = AlbumsAdapterVertical(it)
                .setObjectAction(requireParentFragment().parentFragment as MainFragment)
            binding.rcvArtistPopularAlbums.adapter = adapter
        }

        return binding.root
    }
}