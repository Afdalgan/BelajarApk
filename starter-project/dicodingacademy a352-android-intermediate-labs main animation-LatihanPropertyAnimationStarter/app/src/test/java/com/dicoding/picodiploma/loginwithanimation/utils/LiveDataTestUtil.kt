package com.dicoding.picodiploma.loginwithanimation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { value ->
            data = value
            latch.countDown()
        }
        this.observeForever(observer)
        try {
            afterObserve.invoke()
            if (!latch.await(time, timeUnit)) {
                throw IllegalStateException("LiveData value was never set.")
            }
        } finally {
            this.removeObserver(observer)
        }
        @Suppress("UNCHECKED_CAST")
        return data as T
    }
