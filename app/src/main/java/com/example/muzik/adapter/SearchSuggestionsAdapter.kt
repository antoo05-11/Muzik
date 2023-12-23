package com.example.muzik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.ui.search_fragment.SearchFragment

class SearchSuggestionsAdapter(var suggestions: List<String>) :
    RecyclerView.Adapter<SearchSuggestionsAdapter.ViewHolder>() {

    private var fragmentOwner: Fragment? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchSuggestTextView: TextView = itemView.findViewById(R.id.search_hint_text_view)
        val insertSearchTextButton: ImageButton = itemView.findViewById(R.id.insert_search_text_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.searchSuggestTextView.text = suggestion
        holder.itemView.setOnClickListener {
            (fragmentOwner as SearchFragment).search(suggestion)
        }
        holder.insertSearchTextButton.setOnClickListener {
            (fragmentOwner as SearchFragment).insertSearchText(suggestion)
        }
    }

    fun setFragmentOwner(fragmentOwner: Fragment?): SearchSuggestionsAdapter {
        this.fragmentOwner = fragmentOwner
        return this
    }
}