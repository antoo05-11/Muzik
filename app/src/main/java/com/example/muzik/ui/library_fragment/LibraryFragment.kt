package com.example.muzik.ui.library_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.ui.library_fragment.lib_album_fragment.LibAlbumsFragment
import com.example.muzik.ui.library_fragment.lib_artist_fragment.LibArtistFragment
import com.example.muzik.ui.library_fragment.lib_playlist_fragment.LibPlaylistFragment
import com.example.muzik.ui.library_fragment.lib_song_fragment.LibSongsFragment

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

    class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {

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

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

    }

    override fun onResume() {
        super.onResume()
        initViewPager()
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