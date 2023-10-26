package com.example.muzik.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.muzik.MainActivity.Companion.quotesApi
import com.example.muzik.databinding.FragmentCollectionBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CollectionFragment : Fragment() {
    private var _binding: FragmentCollectionBinding? = null

    private val binding get() = _binding!!

    private var response: String = "";

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCollectionBinding.inflate(inflater, container, false)

        // launching a new coroutine
        GlobalScope.launch {
            val result = quotesApi.getQuotes()
            Log.d("ayush: ", result.body().toString())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}