package com.example.socialnetworking.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val onReachedLastPosition: () -> Unit
): RecyclerView.OnScrollListener(){
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if(dy > 0 && ( layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() ) >= layoutManager.itemCount){
            onReachedLastPosition()
        }
    }
}