package com.example.muzik.ui.fragment.library_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.ui.fragment.library_fragment.lib_album_fragment.LibAlbumsFragment
import com.example.muzik.ui.fragment.library_fragment.lib_artist_fragment.LibArtistFragment
import com.example.muzik.ui.fragment.library_fragment.lib_playlist_fragment.LibPlaylistFragment
import com.example.muzik.ui.fragment.library_fragment.lib_song_fragment.LibSongsFragment

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding

    private fun initViewPager() {
        val viewPager = binding.vpLibraryViewPager
        val tabLayout = binding.tlLibraryMainTabLayout
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)

        viewPagerAdapter.add(
            LibSongsFragment(),
            title = "Songs"
        )

        viewPagerAdapter.add(
            LibPlaylistFragment(),
            title = "Playlists"
        )

        viewPagerAdapter.add(
            LibAlbumsFragment(),
            title = "Albums"
        )

        viewPagerAdapter.add(
            LibArtistFragment(),
            title = "Artists"
        )

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        initViewPager()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized) {
            outState.putInt("viewPagerPosition", binding.vpLibraryViewPager.currentItem)
        }
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments: MutableList<Fragment> = ArrayList()
        private val titles: MutableList<String> = ArrayList()

        fun add(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getPageTitle(position: Int): CharSequence = titles[position]
    }
}
