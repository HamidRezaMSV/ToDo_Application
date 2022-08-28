package com.sm.todo.fragments.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToDelete :ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean { return false } // we don't need this so return false

    // we could use ItemTouchHelper directly but we should override two methods (Move and Swipe)
    // so we handle onMove here because we do not need that and override onSwiped in ListFragment

}