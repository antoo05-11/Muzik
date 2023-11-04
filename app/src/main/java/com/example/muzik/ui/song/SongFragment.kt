package com.example.muzik.ui.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.muzik.databinding.FragmentSongBinding

class SongFragment: Fragment() {

    private var _binding: FragmentSongBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}