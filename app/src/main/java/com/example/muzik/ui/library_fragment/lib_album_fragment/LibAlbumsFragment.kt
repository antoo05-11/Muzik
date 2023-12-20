package com.example.muzik.ui.library_fragment.lib_album_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.muzik.databinding.FragmentLibAlbumsBinding

class LibAlbumsFragment: Fragment() {
    private lateinit var binding: FragmentLibAlbumsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibAlbumsBinding.inflate(inflater)
        return binding.root
    }
}