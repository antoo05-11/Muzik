package com.example.muzik.ui.fragment.library_fragment.lib_artist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.artists.ArtistsAdapterVertical
import com.example.muzik.databinding.FragmentLibArtistBinding
import com.example.muzik.ui.fragment.main_fragment.MainFragment
import kotlinx.coroutines.launch

class LibArtistFragment : Fragment() {

    private lateinit var binding: FragmentLibArtistBinding
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var artistAdapter: ArtistsAdapterVertical

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibArtistBinding.inflate(inflater)
        artistViewModel = ViewModelProvider(requireActivity())[ArtistViewModel::class.java]

        artistAdapter = ArtistsAdapterVertical()
        artistAdapter.setObjectAction(requireParentFragment().requireParentFragment().parentFragment as MainFragment)

        binding.rcvArtistsList.adapter = artistAdapter
        binding.rcvArtistsList.layoutManager = LinearLayoutManager(context)

        artistViewModel.artists.observe(viewLifecycleOwner) {
            artistAdapter.updateList(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            artistViewModel.fetchData()
        }
    }
}