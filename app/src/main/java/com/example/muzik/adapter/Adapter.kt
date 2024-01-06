package com.example.muzik.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.ui.Action

abstract class Adapter<VH : RecyclerView.ViewHolder, X>(list: List<X>) :
    RecyclerView.Adapter<VH>() {

    protected var action: Action? = null
        private set

    protected var list: List<X> = mutableListOf()

    init {
        this.list = list
    }

    fun setObjectAction(action: Action): Adapter<VH, X> {
        this.action = action
        return this
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<X> = mutableListOf()) {
        this.list = list
        notifyDataSetChanged()
    }

    fun addItemToList(item: X) {
        (list as MutableList).add(item)
        notifyItemInserted(list.size - 1)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
