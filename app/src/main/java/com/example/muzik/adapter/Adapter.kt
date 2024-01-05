package com.example.muzik.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.data_model.standard_model.Model
import com.example.muzik.ui.main_fragment.MainAction

abstract class Adapter<VH : RecyclerView.ViewHolder, X : Model>(list: List<X>) :
    RecyclerView.Adapter<VH>() {

    protected var mainAction: MainAction? = null
    protected var list: List<X> = mutableListOf()

    init {
        this.list = list
    }

    fun setObjectAction(mainAction: MainAction): Adapter<VH, X> {
        this.mainAction = mainAction
        return this
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<X> = mutableListOf()) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
