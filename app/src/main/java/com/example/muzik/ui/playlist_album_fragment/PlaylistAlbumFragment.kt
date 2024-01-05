package com.example.muzik.ui.playlist_album_fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentPlaylistAlbumBinding
import com.example.muzik.storage.SharedPrefManager
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

    private var album: Album? = null
    private var playlist: Playlist? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = requireArguments().getSerializable("playlist") as? Playlist
        album = requireArguments().getSerializable("album") as? Album

        var imageUri: Uri? = null

        playlist?.let {
            imageUri = it.imageURI
            lifecycleScope.launch {
                viewModel.fetchSongs(
                    it.requirePlaylistID(),
                    PlaylistAlbumViewModel.Type.PLAYLIST
                )
            }
        }
        album?.let {
            imageUri = it.imageURI
            lifecycleScope.launch {
                viewModel.fetchSongs(
                    it.requireAlbumID(),
                    PlaylistAlbumViewModel.Type.ALBUM
                )
            }
        }

        Picasso.get().load(imageUri)
            .into(binding.mainPlaylistAlbumImageView, object : Callback {
                override fun onSuccess() {
                    val imageBitmap: Bitmap =
                        (binding.mainPlaylistAlbumImageView.drawable as BitmapDrawable).bitmap
                    val backgroundDominantColor =
                        PaletteUtils.getDominantGradient(bitmap = imageBitmap, endColor = "#303030")
                    parentFragment?.view?.background = (backgroundDominantColor)
                }

                override fun onError(e: Exception?) {
                }
            })
    }

    override fun onPause() {
        super.onPause()
        parentFragment?.view?.setBackgroundColor(Color.TRANSPARENT)
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
            SongsAdapterVertical::class.java,
            Song::class.java,
            5
        )

//        val mainFragmentNavHostFragment = parentFragment as NavHostFragment
//        val mainFragmentNavController = mainFragmentNavHostFragment.navController

        binding.backButton.setOnClickListener {
            parentFragment?.childFragmentManager?.popBackStack()
        }

        viewModel.playlistAlbumsList.observe(viewLifecycleOwner) {
            val adapter =
                SongsAdapterVertical(it).setFragmentOwner(this).setPlayerViewModel(playerViewModel)
            if (it.size > 1) {
                adapter.hasItemIndexTextView()
            }

            binding.rcvSongsInsidePlaylistAlbumView.adapter = adapter

            album?.let { album ->
                adapter.hasViewsShowed()
                binding.artistsListTextview.text = album.artistName
                binding.playlistAlbumNameTextview.text = album.name
            }

            playlist?.let { playlist ->
                binding.artistsListTextview.text =
                    SharedPrefManager.getInstance(requireActivity()).user.name
                binding.playlistAlbumNameTextview.text = playlist.name
            }
        }

        return binding.root
    }
}