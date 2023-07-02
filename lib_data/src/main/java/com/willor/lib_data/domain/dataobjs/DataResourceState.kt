package com.willor.lib_data.domain.dataobjs

sealed class DataResourceState<T>{
    class Loading<T>: DataResourceState<T>()
    class Success<T>(val data: T): DataResourceState<T>()
    class Error<T>(val msg: String, val exception: Exception? = null): DataResourceState<T>()
}

