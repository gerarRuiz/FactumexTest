package com.ruiz.emovie.util.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/** Updates the MutableLiveData.value atomically using the specified function of its value. **/
inline fun <T> MutableLiveData<T>.update(function: (T?) -> T?) {
    val newValue = function(value)
    this.value = newValue
}

/** Represents this mutable live data as live data. **/
fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> {
    return this
}