package com.example.muzik.ui.search_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.adapter.SearchSuggestionsAdapter
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentSearchBinding
import com.example.muzik.music_service.LocalMusicRepository
import com.example.muzik.ui.player_view_fragment.PlayerViewModel
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding

    class SearchAdapterVertical : SongsAdapterVertical(LocalMusicRepository.getSongs()),
        Filterable {
        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val str: String = constraint.toString()
                    songsPreviewList = if (str.isEmpty()) {
                        LocalMusicRepository.getSongs()
                    } else {
                        val list: MutableList<Song> = ArrayList()
                        for (song in LocalMusicRepository.getSongs()) {
                            if (song.name?.lowercase()
                                    ?.contains(str.lowercase()) == true || song.artistName?.lowercase()
                                    ?.contains(str.lowercase()) == true
                            ) {
                                list.add(song)
                            }
                        }
                        list
                    }

                    val filterResult = FilterResults()
                    filterResult.values = songsPreviewList
                    return filterResult
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    if (results != null) {
                        songsPreviewList = results.values as List<Song>
                        notifyDataSetChanged()
                    }
                }

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        val playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

//        val adapter = SearchAdapterVertical().setFragmentOwner(this)
//            .setPlayerViewModel(playerViewModel) as SearchAdapterVertical
//
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(context)
//        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                adapter.filter.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.filter.filter(newText)
//                return false
//            }
//        })

        val songs = mutableListOf<Song>()
        val adapter =
            SongsAdapterVertical(songs).setPlayerViewModel(playerViewModel).setFragmentOwner(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        searchViewModel.songList.observe(viewLifecycleOwner) {
            songs.clear()
            for (song in it) songs.add(song)
            adapter.notifyDataSetChanged()
        }

        val suggestionsAdapter = SearchSuggestionsAdapter(mutableListOf()).setFragmentOwner(this)
        binding.searchHintRcv.adapter = suggestionsAdapter
        binding.searchHintRcv.layoutManager = LinearLayoutManager(context)

        searchViewModel.suggestionList.observe(viewLifecycleOwner) {
            suggestionsAdapter.suggestions = it
            suggestionsAdapter.notifyDataSetChanged()
        }

        binding.svSearchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchHintScrollView.visibility = View.VISIBLE
                binding.searchScrollView.visibility = View.INVISIBLE
                binding.searchBackButton.visibility = View.VISIBLE
            } else {
                binding.searchHintScrollView.visibility = View.INVISIBLE
                binding.searchScrollView.visibility = View.VISIBLE
                binding.searchBackButton.visibility = View.GONE
            }
        }

        binding.searchBackButton.setOnClickListener {
            binding.svSearchView.clearFocus()
        }

        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(s: String): Boolean {
                lifecycleScope.launch {
                    searchViewModel.fetchSearchSuggestions(searchText = binding.svSearchView.query.toString())
                }
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                search(searchText = binding.svSearchView.query.toString())
                return true
            }
        })

        return binding.root
    }

    fun search(searchText: String = "") {
        lifecycleScope.launch {
            searchViewModel.fetchSearchSongs(true, searchText)
        }
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
        binding.svSearchView.isFocusable = false
        binding.svSearchView.clearFocus()
    }

    fun insertSearchText(searchText: String = "") {
        binding.svSearchView.setQuery(searchText, false)
    }
}