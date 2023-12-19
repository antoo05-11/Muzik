package com.example.muzik.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.navigation.NavHostController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.data_model.standard_model.Model
import java.io.Serializable

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

fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>, X : Model> addSampleForRcv(
    rcv: RecyclerView,
    adapterClazz: Class<T>,
    itemClazz: Class<X>,
    sampleSize: Int,
    navHostController: NavHostController? = null
) {
    val itemConstructor = itemClazz.getConstructor()
    val list = List(sampleSize) { itemConstructor.newInstance() }

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

fun setRotateAnimation(view: View): ObjectAnimator {
    val anim = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f).setDuration(12000)
    anim.repeatCount = ValueAnimator.INFINITE
    anim.interpolator = LinearInterpolator()
    anim.start()
    return anim
}

fun <T: Serializable> printLogcat(string: T) {
    Log.e("app-debug", string.toString())
}