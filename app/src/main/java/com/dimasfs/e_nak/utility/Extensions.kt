package com.dimasfs.e_nak.utility

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


object Extensions{
    fun rupiahFormat(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return "Rp " + formatter.format(price.toLong())
    }

    fun View.animateVisibility(isVisible: Boolean, duration: Long = 400) {
        ObjectAnimator
            .ofFloat(this, View.ALPHA, if (isVisible) 1f else 0f)
            .setDuration(duration)
            .start()
    }

    fun TextView.setLocalDateFormat(timestamp: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = sdf.parse(timestamp) as Date

        val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
        this.text = formattedDate
    }
}


//@OptIn(ExperimentalCoroutinesApi::class)
//suspend fun <T> Task<T>.await():T{
//    return suspendCancellableCoroutine {
//        addOnCompleteListener{ task ->
//            if(task.exception == null) {
//                it.resume(task.result, null)
//            } else {
//                it.resumeWithException(task.exception!!)
//            }
//        }
//    }
