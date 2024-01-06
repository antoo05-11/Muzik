package com.example.muzik.ui.fragment.search_fragment

import com.example.muzik.ui.Action

interface SearchAction : Action {
    fun search(searchText: String)
    fun insertSearchText(searchText: String)
}