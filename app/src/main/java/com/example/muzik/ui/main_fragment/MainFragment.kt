package com.example.muzik.ui.main_fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.muzik.R
import com.example.muzik.databinding.FragmentMainBinding
import com.example.muzik.ui.lib_song_fragment.SongViewModel
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.ui.search_fragment.SearchViewModel
import com.example.muzik.utils.PaletteUtils
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var songViewModel: SongViewModel

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var searchViewModel: SearchViewModel

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
        val mainFragmentNavController = mainFragmentNavHostFragment.navController
        binding.bottomNavView.setupWithNavController(mainFragmentNavController)

        searchViewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        searchViewModel.opened.observe(requireActivity()) {
            if (it) {
                mainFragmentNavController.popBackStack()
                mainFragmentNavController.navigate(R.id.navigation_search)
                searchViewModel.opened.value = false
            }
        }

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
            binding.pbSongPreview.max = it.duration!!
        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            binding.pbSongPreview.progress = it
        }

        playerViewModel.isSelectedMutableLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.clPreview.visibility = View.VISIBLE
                val navView = mainFragmentNavHostFragment.view
                navView?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomToTop = binding.clPreview.id
                }
            }
        }

        val mainNavHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.fragment_main_nav) as NavHostFragment
        val mainNavController = mainNavHostFragment.navController

        binding.clPreview.setOnClickListener {
            mainNavController.navigate(
                R.id.playerViewFragment, null, NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_up)
                    .setExitAnim(R.anim.slide_out_up)
                    .setPopEnterAnim(R.anim.slide_in_up)
                    .setPopExitAnim(R.anim.slide_out_up)
                    .build()
            )
        }

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        mainFragmentNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_explore -> {
                    supportActionBar?.title = "Explore"
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

                else -> {
                    supportActionBar?.hide()
                    return@addOnDestinationChangedListener
                }
            }
        }
        return binding.root
    }
}