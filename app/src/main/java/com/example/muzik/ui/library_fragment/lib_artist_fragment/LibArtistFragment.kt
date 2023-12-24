package com.example.muzik.ui.library_fragment.lib_artist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.artists.ArtistsAdapterVertical
import com.example.muzik.databinding.FragmentLibArtistBinding
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

        val navHostController =
            parentFragment?.parentFragment?.childFragmentManager?.findFragmentById(R.id.fragment_lib_nav)
                ?.findNavController() as NavHostController

        artistAdapter =
            ArtistsAdapterVertical(
                mutableListOf(),
                navHostController
            )

        binding.rcvArtistsList.adapter = artistAdapter
        binding.rcvArtistsList.layoutManager = LinearLayoutManager(context)

        artistViewModel.artists.observe(viewLifecycleOwner) {
            artistAdapter.updateArtistList(it)
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