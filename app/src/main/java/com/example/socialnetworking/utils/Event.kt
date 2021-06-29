package com.example.socialnetworking.utils

class Event<T>(
    private val _data: T?
){
    private var isHandled: Boolean = false

    val data: T?
    get() {
        return if(isHandled) null
        else {
            isHandled = true
            _data
        }
    }
}