package com.example.muzik.ui.main_fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.muzik.R
import com.example.muzik.data_model.standard_model.Album
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Playlist
import com.example.muzik.databinding.FragmentMainBinding
import com.example.muzik.ui.bottom_sheet_dialog.songs.SongOptionsBottomSheet
import com.example.muzik.ui.create_playlist_activity.CreatePlaylistActivity
import com.example.muzik.ui.library_fragment.lib_song_fragment.SongViewModel
import com.example.muzik.ui.player_view_fragment.PlayerViewFragment
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.ui.search_fragment.SearchViewModel
import com.example.muzik.utils.PaletteUtils
import com.example.muzik.utils.printLogcat
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class MainFragment : Fragment(), MainAction {

    private lateinit var binding: FragmentMainBinding

    private lateinit var songViewModel: SongViewModel

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var mainFragmentNavController: NavHostController

    companion object {
        val modalBottomSheet = SongOptionsBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        songViewModel = ViewModelProvider(requireActivity())[SongViewModel::class.java]
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]


        val mainFragmentNavHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_lib_nav) as NavHostFragment
        mainFragmentNavController = mainFragmentNavHostFragment.navController as NavHostController
        binding.bottomNavView.setupWithNavController(mainFragmentNavController)

        binding.bottomNavView.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.bottomNavView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mainFragmentNavHostFragment.view?.updatePadding(
                    bottom = binding.bottomNavView.height.plus(5)
                )
            }
        })

        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        playerViewModel.playingMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.previewPlayingStateButton.setBackgroundResource(R.drawable.icon_pause)
            } else {
                binding.previewPlayingStateButton.setBackgroundResource(R.drawable.icon_play)
            }
        }
        binding.previewPlayingStateButton.setOnClickListener {
            playerViewModel.playPause()
        }

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            binding.tvSongNamePreview.text = it.name
            binding.artistUnderPlayerPreviewTextview.text = it.artistName
            if (it.imageURI == null) {
                binding.shimmerSongImageUnderSongPreview.hideShimmer()
                binding.songImageUnderSongPreview.setBackgroundResource(R.drawable.icons8_song_50)

                val color = ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                val gradientDrawable = GradientDrawable()
                gradientDrawable.setColor(color)
                gradientDrawable.cornerRadius = 15f
                ViewCompat.setBackground(binding.clPreview, gradientDrawable)
            }
            Picasso.get().load(it.imageURI)
                .into(binding.songImageUnderSongPreview, object : Callback {
                    override fun onSuccess() {
                        binding.shimmerSongImageUnderSongPreview.hideShimmer()

                        val imageBitmap: Bitmap =
                            (binding.songImageUnderSongPreview.drawable as BitmapDrawable).bitmap
                        val backgroundDominantColor =
                            PaletteUtils.getDominantGradient(
                                imageBitmap,
                                15.0f,
                                GradientDrawable.Orientation.TL_BR, null
                            )
                        binding.clPreview.background = (backgroundDominantColor)
                    }

                    override fun onError(e: Exception?) {

                    }

                })
            binding.pbSongPreview.max = it.duration
        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            binding.pbSongPreview.progress = it
        }

        playerViewModel.isSelectedMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.clPreview.visibility = View.VISIBLE
                binding.artistUnderPlayerPreviewTextview.isFocusable = true
                binding.tvSongNamePreview.isFocusable = true
                mainFragmentNavHostFragment.view?.updatePadding(
                    bottom = binding.bottomNavView.height.plus(
                        binding.clPreview.maxHeight
                    ).plus(5)
                )
            }
        }

        val playerViewFragment = PlayerViewFragment()
        binding.clPreview.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_main_nav, playerViewFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        mainFragmentNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_explore -> {
                    supportActionBar?.title = "Explore"
                    printLogcat("click")
                    supportActionBar?.show()
                }

                R.id.navigation_chart -> {
                    supportActionBar?.title = "Chart"
                    supportActionBar?.show()
                }

                R.id.navigation_search -> {
                    supportActionBar?.title = "Search"
                    supportActionBar?.show()
                }

                R.id.navigation_library -> {
                    supportActionBar?.title = "Library"
                    supportActionBar?.show()
                }

                R.id.navigation_stream_share -> {
                    supportActionBar?.title = "Stream share"
                    supportActionBar?.show()
                }

                else -> {
                    supportActionBar?.hide()
                    return@addOnDestinationChangedListener
                }
            }
        }
        return binding.root
    }

    override fun goToArtistFragment(artistID: Long?, artist: Artist?) {
        val bundle = Bundle().apply {
            artistID?.let { putLong("artistID", it) }
            artist?.let { putSerializable("artist", it) }
        }
        mainFragmentNavController.navigate(R.id.artistFragment, bundle)
    }

    override fun goToPlaylistFragment(playlistID: Long?, playlist: Playlist?) {
        val bundle = Bundle().apply {
            playlist?.let { putSerializable("playlist", it) }
            playlistID?.let { putLong("playlistID", it) }
        }
        mainFragmentNavController.navigate(R.id.playlistAlbumFragment, bundle)
    }

    override fun goToAlbumFragment(albumID: Long?, album: Album?) {
        val bundle = Bundle().apply {
            album?.let { putSerializable("album", it) }
            albumID?.let { putLong("albumID", it) }
        }
        mainFragmentNavController.navigate(R.id.playlistAlbumFragment, bundle)
    }

    override fun goToCreatePlaylistActivity() {
        context?.startActivity(
            Intent(
                context,
                CreatePlaylistActivity::class.java
            )
        )
    }

    override fun addSongToPlaylist(playlistID: Long) {
        modalBottomSheet.playlistsBottomSheet.addSongToPlaylist(playlistID)
    }
}