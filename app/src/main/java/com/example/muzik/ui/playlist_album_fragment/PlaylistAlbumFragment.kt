package com.example.muzik.ui.playlist_album_fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.muzik.adapter.ListSongsPreviewAdapter
import com.example.muzik.databinding.FragmentPlaylistAlbumBinding
import com.example.muzik.response_model.Song
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.utils.PaletteUtils
import com.example.muzik.utils.addDecorationForVerticalRcv
import com.example.muzik.utils.addSampleForRcv
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class PlaylistAlbumFragment : Fragment() {
    private lateinit var viewModel: PlaylistAlbumViewModel
    private lateinit var binding: FragmentPlaylistAlbumBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchSongs(
                requireArguments().getInt("playlistAlbumID"),
                requireArguments().getSerializable("type") as PlaylistAlbumViewModel.Type,
                requireContext()
            )
        }
        Picasso.get().load(requireArguments().getString("playlistAlbumImageURL"))
            .into(binding.mainPlaylistAlbumImageView, object : Callback {
                override fun onSuccess() {
                    val imageBitmap: Bitmap =
                        (binding.mainPlaylistAlbumImageView.drawable as BitmapDrawable).bitmap
                    val backgroundDominantColor =
                        PaletteUtils.getDominantGradient(imageBitmap, null, null, "#303030")
                    binding.mainImageContainer.background = (backgroundDominantColor)
                }

                override fun onError(e: Exception?) {
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistAlbumBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PlaylistAlbumViewModel::class.java]
        val playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

        addDecorationForVerticalRcv(binding.rcvSongsInsidePlaylistAlbumView, requireActivity())
        addSampleForRcv(
            binding.rcvSongsInsidePlaylistAlbumView,
            ListSongsPreviewAdapter::class.java,
            Song::class.java,
            5
        )

        val mainFragmentNavHostFragment = parentFragment as NavHostFragment
        val mainFragmentNavController = mainFragmentNavHostFragment.navController

        binding.backButton.setOnClickListener {
            mainFragmentNavController.popBackStack()
        }

        viewModel.playlistAlbumsList.observe(viewLifecycleOwner) {
            val adapter = ListSongsPreviewAdapter(it).setFragmentOwner(this).setPlayerViewModel(playerViewModel)
            if (it.size > 1) {
                adapter.hasItemIndexTextView()
            }
            if (requireArguments().getSerializable("type") as PlaylistAlbumViewModel.Type == PlaylistAlbumViewModel.Type.ALBUM) {
                adapter.hasViewsShowed()
            }
            binding.rcvSongsInsidePlaylistAlbumView.adapter = adapter

            binding.playlistAlbumNameTextview.text =
                requireArguments().getString("playlistAlbumName")
            binding.artistsListTextview.text = requireArguments().getString("albumArtistName")

            for (song in it) {
                song.artistName = requireArguments().getString("albumArtistName")
            }
        }

        return binding.root
    }
}