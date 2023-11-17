package com.example.muzik.ui.search_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    var opened: MutableLiveData<Boolean> = MutableLiveData(false)
        private set

    init {

    }
}