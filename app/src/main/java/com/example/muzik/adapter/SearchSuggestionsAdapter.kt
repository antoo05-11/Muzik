package com.example.muzik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.ui.fragment.search_fragment.SearchAction

class SearchSuggestionsAdapter(suggestions: List<String> = mutableListOf()) :
    Adapter<SearchSuggestionsAdapter.ViewHolder, String>(suggestions) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchSuggestTextView: TextView = itemView.findViewById(R.id.search_hint_text_view)
        val insertSearchTextButton: ImageButton =
            itemView.findViewById(R.id.insert_search_text_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = list[position]
        holder.searchSuggestTextView.text = suggestion
        holder.itemView.setOnClickListener {
            (action as? SearchAction)?.search(searchText = suggestion)
        }
        holder.insertSearchTextButton.setOnClickListener {
            (action as? SearchAction)?.insertSearchText(searchText = suggestion)
        }
    }
}