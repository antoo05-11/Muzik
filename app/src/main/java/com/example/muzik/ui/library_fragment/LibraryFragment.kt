package com.example.muzik.ui.library_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    private lateinit var viewModel: LibraryViewModel
    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }
}