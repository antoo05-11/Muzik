package com.example.muzik.ui.library_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.ListArtistAdapter
import com.example.muzik.adapter.ListPlaylistAdapter
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.response_model.Playlist
import com.example.muzik.ui.lib_album_fragment.LibAlbumsFragment
import com.example.muzik.ui.lib_artist_fragment.LibArtistFragment
import com.example.muzik.ui.lib_playlist_fragment.LibPlaylistFragment
import com.example.muzik.ui.lib_song_fragment.LibSongsFragment
import com.example.muzik.utils.addDecorationForVerticalRcv

class LibraryFragment : Fragment() {

    private lateinit var viewModel: LibraryViewModel
    private lateinit var binding: FragmentLibraryBinding

    private fun initViewPager() {
        val viewPager = binding.vpLibraryViewPager
        val tabLayout = binding.tlLibraryMainTabLayout
        val viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.add(LibSongsFragment(), "Songs")
        viewPagerAdapter.add(LibPlaylistFragment(), "Playlists")
        viewPagerAdapter.add(LibAlbumsFragment(), "Albums")
        viewPagerAdapter.add(LibArtistFragment(), "Artists")
        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = viewPagerAdapter
    }

    class ViewPagerAdapter(private val fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

        private val fragments: MutableList<Fragment> = ArrayList()
        private val titles: MutableList<String> = ArrayList()

        fun add(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        initViewPager()
        return binding.root
    }


}