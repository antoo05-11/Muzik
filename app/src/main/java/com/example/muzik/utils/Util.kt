package com.example.muzik.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.CustomItemDecoration
import com.example.muzik.response_model.ResponseModel

fun getReadableTime(time: Int): String {
    var s: Int = time / 1000
    val m = s / 60
    s %= 60
    return "$m:${s / 10}${s % 10}"
}

fun addDecorationForHorizontalRcv(recyclerView: RecyclerView, activity: Activity) {
    recyclerView.layoutManager =
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.addItemDecoration(
        CustomItemDecoration(30)
    )
}

fun addDecorationForVerticalRcv(recyclerView: RecyclerView, activity: Activity) {
    recyclerView.layoutManager = LinearLayoutManager(activity)
}

fun addDecorationForHorizontalRcv(rcvList: List<RecyclerView>, activity: Activity) {
    rcvList.forEach {
        addDecorationForHorizontalRcv(it, activity)
    }
}

fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>, X : ResponseModel> addSampleForRcv(
    rcv: RecyclerView,
    adapterClazz: Class<T>,
    itemClazz: Class<X>,
    sampleSize: Int
) {
    val adapterConstructor = adapterClazz.getConstructor(MutableList::class.java)
    val itemConstructor = itemClazz.getConstructor(Boolean::class.java)
    val list = List(sampleSize) { itemConstructor.newInstance(true) }
    val adapter = adapterConstructor.newInstance(list)
    rcv.adapter = adapter
}

fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>, X : ResponseModel> addSampleForRcv(
    rcvList: List<Triple<RecyclerView, Class<T>, Class<X>>>,
    sampleSize: Int
) {
    rcvList.forEach { (rcv, adapterClazz, itemClazz) ->
        addSampleForRcv(rcv, adapterClazz, itemClazz, sampleSize)
    }

}

fun showNotification(context: Context, content: Any) {
    Toast.makeText(context, content.toString(), Toast.LENGTH_SHORT).show()
}
