package com.example.muzik.ui.fragment.library_fragment.lib_album_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.albums.AlbumsAdapterVertical
import com.example.muzik.databinding.FragmentLibAlbumsBinding
import com.example.muzik.ui.fragment.main_fragment.MainFragment
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
        viewModel = ViewModelProvider(requireActivity())[AlbumViewModel::class.java]

        adapter = AlbumsAdapterVertical()

        adapter.setObjectAction(requireParentFragment().requireParentFragment().parentFragment as MainFragment)
        binding.rcvAlbums.adapter = adapter
        binding.rcvAlbums.layoutManager = LinearLayoutManager(context)

        viewModel.albums.observe(viewLifecycleOwner) {
            adapter.updateList(it)
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