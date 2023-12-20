package com.example.muzik.ui.library_fragment.lib_artist_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.artists.ArtistsAdapterVertical
import com.example.muzik.data_model.standard_model.Artist
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentLibArtistBinding
import com.example.muzik.ui.library_fragment.lib_song_fragment.SongViewModel

class LibArtistFragment : Fragment() {
    private lateinit var binding: FragmentLibArtistBinding
    private lateinit var songViewModel: SongViewModel
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var artistAdapter: ArtistsAdapterVertical

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibArtistBinding.inflate(inflater)
        songViewModel = ViewModelProvider(requireActivity())[SongViewModel::class.java]
        songViewModel.songsMutableLiveData.observe(viewLifecycleOwner) {
            artistAdapter =
                ArtistsAdapterVertical(
                    getArtistsFromSongs(it),
                    null
                )
            binding.rcvArtistsList.adapter = artistAdapter
            binding.rcvArtistsList.layoutManager = LinearLayoutManager(this.context)
        }
        return binding.root
    }

    private fun getArtistsFromSongs(songs: List<Song>): List<Artist> {
        val artistList = mutableListOf<Artist>()
        val idSet: HashSet<Long> = HashSet()
        val mapArtist = songViewModel.artistsLiveData
        for (song in songs) {
            if (!idSet.contains(song.artistID)) {
                artistList.add(mapArtist.value?.get(song.artistID!!)!!)
                idSet.add(song.artistID!!);
            }
        }
        return artistList
    }
}