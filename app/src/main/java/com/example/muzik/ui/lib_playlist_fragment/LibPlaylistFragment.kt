package com.example.muzik.ui.lib_playlist_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.muzik.databinding.FragmentLibPlaylistBinding

class LibPlaylistFragment: Fragment() {

    private lateinit var binding: FragmentLibPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibPlaylistBinding.inflate(inflater)
        return binding.root
    }
}