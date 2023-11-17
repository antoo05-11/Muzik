package com.example.muzik.ui.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.muzik.R
import com.example.muzik.databinding.FragmentMainBinding
import com.example.muzik.ui.lib_song_fragment.SongViewModel
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import com.example.muzik.ui.search_fragment.SearchViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private lateinit var songViewModel: SongViewModel

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var searchViewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        playerViewModel.songMutableLiveData.observe(viewLifecycleOwner) {
            binding.tvSongNamePreview.text = it.name
            binding.pbSongPreview.max = it.duration!!
        }

        playerViewModel.currentTimeMutableLiveData.observe(viewLifecycleOwner) {
            binding.pbSongPreview.progress = it
        }

        playerViewModel.isSelectedMutableLiveData.observe(viewLifecycleOwner) { it ->
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
            mainNavController.navigate(R.id.playerViewFragment)
        }

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        mainFragmentNavController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = when (destination.id) {
                R.id.navigation_explore -> "Explore"
                R.id.navigation_chart -> "Chart"
                R.id.navigation_search -> "Search"
                R.id.navigation_library -> "Library"
                else -> {
                    supportActionBar?.hide()
                    return@addOnDestinationChangedListener
                }
            }
        }

        return binding.root
    }
}