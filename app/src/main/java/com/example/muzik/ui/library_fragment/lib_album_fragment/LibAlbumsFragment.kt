package com.example.muzik.ui.library_fragment.lib_album_fragment

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
import com.example.muzik.adapter.albums.AlbumsAdapterVertical
import com.example.muzik.databinding.FragmentLibAlbumsBinding
import kotlinx.coroutines.launch

class LibAlbumsFragment : Fragment() {

    private lateinit var binding: FragmentLibAlbumsBinding
    private lateinit var viewModel: AlbumViewModel

    private lateinit var adapter: AlbumsAdapterVertical

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibAlbumsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[AlbumViewModel::class.java]

        val navHostController =
            parentFragment?.parentFragment?.childFragmentManager?.findFragmentById(R.id.fragment_lib_nav)
                ?.findNavController() as NavHostController
        adapter = AlbumsAdapterVertical(mutableListOf(), navHostController)
        binding.rcvAlbums.adapter = adapter
        binding.rcvAlbums.layoutManager = LinearLayoutManager(context)

        viewModel.albums.observe(viewLifecycleOwner) {
            adapter.albums = it
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.fetchData()
        }
    }
}