package com.example.muzik.ui.explore_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.CustomItemDecoration
import com.example.muzik.adapter.ListPlaylistsPreviewAdapter
import com.example.muzik.databinding.FragmentExploreBinding
import com.example.muzik.response_model.Playlist
import java.util.Date

class ExploreFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var binding: FragmentExploreBinding

    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        val playlists = mutableListOf(
            Playlist(1, "a", 1, "a", Date(20031105.toLong())),
            Playlist(1, "a", 1, "a", Date(20031105.toLong())),
            Playlist(1, "a", 1, "a", Date(20031105.toLong())),
            Playlist(1, "a", 1, "a", Date(20031105.toLong())),
            Playlist(1, "a", 1, "a", Date(20031105.toLong())),
            Playlist(1, "a", 1, "a", Date(20031105.toLong()))
        )
        val listPlaylistsPreviewAdapter = ListPlaylistsPreviewAdapter(playlists);
        binding.rcvPlaylistsPreview.adapter = listPlaylistsPreviewAdapter
        binding.rcvPlaylistsPreview.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvPlaylistsPreview.addItemDecoration(
            CustomItemDecoration(18)
        )
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ExploreViewModel::class.java]
    }

}