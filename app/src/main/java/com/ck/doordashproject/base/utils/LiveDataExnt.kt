package com.ck.doordashproject.base.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@MainThread
fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, fn: (T) -> Unit) {
    observe(lifecycleOwner, Observer { it ->
        fn(it)
    })
}

@MainThread
fun <T> LiveData<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, fn: (T) -> Unit) {
    observe(lifecycleOwner, Observer { event ->
        event?.handleContent {
            fn(it)
        }
    })
}

fun <T> MutableLiveData<Event<T>>.fireEvent(eventValue: T) {
    postValue(Event(eventValue))
}

fun MutableLiveData<Event<Unit>>.fireEvent() {
    postValue(Event(Unit))
}

/**
 * Used as a wrapper for data which is exposed via a LiveData tat represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set //Allow external read but not write

    fun handleContent(fn: (T) -> Unit) {
        if (!hasBeenHandled) {
            fn(content)
            hasBeenHandled = true
        }
    }

    /**
     * Returns then content, even if it has already been handled
     */
    fun peekContent() = content
}