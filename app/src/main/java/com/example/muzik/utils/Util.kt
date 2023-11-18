package com.example.muzik.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.navigation.NavHostController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

fun addDecorationForHorizontalRcv(rcvList: List<RecyclerView>, activity: Activity) {
    rcvList.forEach {
        addDecorationForHorizontalRcv(it, activity)
    }
}


fun addDecorationForVerticalRcv(recyclerView: RecyclerView, activity: Activity) {
    recyclerView.layoutManager = LinearLayoutManager(activity)
}

fun addDecorationForVerticalRcv(rcvList: List<RecyclerView>, activity: Activity) {
    rcvList.forEach {
        addDecorationForVerticalRcv(it, activity)
    }
}


fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>, X : ResponseModel> addSampleForRcv(
    rcv: RecyclerView,
    adapterClazz: Class<T>,
    itemClazz: Class<X>,
    sampleSize: Int,
    navHostController: NavHostController? = null
) {
    val itemConstructor = itemClazz.getConstructor(Boolean::class.java)
    val list = List(sampleSize) { itemConstructor.newInstance(true) }

    val adapter: T? = if (navHostController != null) {
        val adapterConstructor =
            adapterClazz.getConstructor(MutableList::class.java, NavHostController::class.java)
        adapterConstructor.newInstance(list, navHostController)
    } else {
        val adapterConstructor = adapterClazz.getConstructor(MutableList::class.java)
        adapterConstructor.newInstance(list)
    }

    rcv.adapter = adapter
}

fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>, X : ResponseModel> addSampleForRcv(
    rcvList: List<Triple<RecyclerView, Class<T>, Class<X>>>,
    sampleSize: Int,
    navHostController: NavHostController? = null
) {
    rcvList.forEach { (rcv, adapterClazz, itemClazz) ->
        addSampleForRcv(rcv, adapterClazz, itemClazz, sampleSize, navHostController)
    }
}

fun showNotification(context: Context, content: Any) {
    Toast.makeText(context, content.toString(), Toast.LENGTH_SHORT).show()
}

fun setRotateAnimation(view: View) {
    val rotate = RotateAnimation(
        0f,
        360f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotate.duration = 10000
    rotate.interpolator = LinearInterpolator()
    rotate.fillAfter = true
    rotate.repeatCount = Animation.INFINITE

    view.startAnimation(rotate)
}
