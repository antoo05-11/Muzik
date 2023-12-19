package com.example.muzik.ui.search_fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.adapter.ListSongsPreviewAdapter
import com.example.muzik.data_model.standard_model.Song
import com.example.muzik.databinding.FragmentSearchBinding
import com.example.muzik.music_service.LocalMusicRepository
import com.example.muzik.ui.main_activity.MainActivity
import com.example.muzik.ui.player_view_fragment.PlayerViewModel

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: FragmentSearchBinding

    class SearchAdapter: ListSongsPreviewAdapter(LocalMusicRepository.getSongs()), Filterable{
        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val str: String = constraint.toString()
                    if(str.isEmpty()) {
                        songsPreviewList = LocalMusicRepository.getSongs()
                    } else {
                        val list: MutableList<Song> = ArrayList()
                        for(song in LocalMusicRepository.getSongs()) {
                            if(song.name?.lowercase()?.contains(str.lowercase()) == true || song.artistName?.lowercase()
                                    ?.contains(str.lowercase()) == true
                                    ) {
                                list.add(song)
                            }
                        }
                        songsPreviewList = list
                    }

                    val filterResult = FilterResults()
                    filterResult.values = songsPreviewList
                    return filterResult
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    if (results != null) {
                        songsPreviewList = results.values as List<Song>
                        notifyDataSetChanged()
                    }
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        val playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        val adapter = SearchAdapter()
        adapter.setFragmentOwner(this)
        adapter.setPlayerViewModel(playerViewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.svSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.svSearchView.post {
            binding.svSearchView.visibility =View.VISIBLE
            binding.svSearchView.isIconified = false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}