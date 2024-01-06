package com.example.muzik.ui.fragment.search_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.R
import com.example.muzik.adapter.SearchSuggestionsAdapter
import com.example.muzik.adapter.SongsAdapterVertical
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentSearchBinding
import com.example.muzik.music_service.LocalMusicRepository
import com.example.muzik.ui.fragment.player_view_fragment.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.launch


class SearchFragment : Fragment(), SearchAction {

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView

    private lateinit var adapter: SongsAdapterVertical

    class SearchAdapterVertical : SongsAdapterVertical(LocalMusicRepository.getSongs()),
        Filterable {
        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val str: String = constraint.toString()
                    list = if (str.isEmpty()) {
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
                    filterResult.values = list
                    return filterResult
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    if (results != null) {
                        updateList(results.values as List<Song>)
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

        searchView = binding.svSearchView
        searchView.queryHint = "What do you want to listen to?"

        val searchViewIcon: ImageView =
            searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)

//        val adapter = SearchAdapterVertical().setFragmentOwner(this)
//            .setPlayerViewModel(playerViewModel) as SearchAdapterVertical
//
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(context)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        adapter =
            SongsAdapterVertical(songs).setPlayerViewModel(playerViewModel).setFragmentOwner(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        searchViewModel.songList.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        val suggestionsAdapter = SearchSuggestionsAdapter().setObjectAction(action = this)
        binding.searchHintRcv.adapter = suggestionsAdapter
        binding.searchHintRcv.layoutManager = LinearLayoutManager(context)

        searchViewModel.suggestionList.observe(viewLifecycleOwner) {
            suggestionsAdapter.updateList(it)
        }

        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                navBar.visibility = View.GONE
                requireParentFragment().view?.updatePadding(
                    bottom = 0
                )
                binding.searchHintScrollView.visibility = View.VISIBLE
                binding.searchScrollViewContainer.visibility = View.INVISIBLE
                binding.searchBackButton.visibility = View.VISIBLE
                binding.searchModeTab.visibility = View.GONE
                searchViewIcon.setImageDrawable(null);
            } else {
                navBar.visibility = View.VISIBLE
                requireParentFragment().view?.updatePadding(
                    bottom = navBar.height.plus(5)
                )
                binding.searchHintScrollView.visibility = View.INVISIBLE
                binding.searchScrollViewContainer.visibility = View.VISIBLE
                binding.searchBackButton.visibility = View.GONE
                binding.searchModeTab.visibility = View.VISIBLE
                searchViewIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_search_24
                    )
                )
            }
        }

        binding.searchBackButton.setOnClickListener {
            searchView.clearFocus()
        }
        binding.searchModeTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) {
                    binding.searchHintRcv.adapter = SearchSuggestionsAdapter()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(s: String): Boolean {
                lifecycleScope.launch {
                    if (binding.searchModeTab.selectedTabPosition == 0 || binding.searchModeTab.selectedTabPosition == 2) {
                        searchViewModel.fetchSearchSuggestions(searchText = searchView.query.toString())
                    } else {

                    }
                }
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                if (binding.searchModeTab.selectedTabPosition == 0 || binding.searchModeTab.selectedTabPosition == 2) {
                    search(searchText = searchView.query.toString())
                } else {
                    search(youtube = false, searchText = searchView.query.toString())
                }
                return true
            }
        })

        return binding.root
    }

    fun search(youtube: Boolean = true, searchText: String = "") {
        searchView.setQuery(searchText, false)
        binding.loadingContainer.root.visibility = View.VISIBLE
        adapter.updateList()
        lifecycleScope.launch {
            searchViewModel.fetchSearchSongs(youtube, searchText)
            binding.loadingContainer.root.visibility = View.INVISIBLE
        }
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        searchView.isFocusable = false
        searchView.clearFocus()
    }

    override fun search(searchText: String) {
        search(youtube = true, searchText = searchText)
    }

    override fun insertSearchText(searchText: String) {
        searchView.setQuery(searchText, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        searchView.clearFocus()
    }
}