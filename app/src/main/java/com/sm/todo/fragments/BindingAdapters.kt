package com.sm.todo.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sm.todo.R
import com.sm.todo.data.models.Priority
import com.sm.todo.data.models.ToDoData
import com.sm.todo.fragments.list.ListFragmentDirections

class BindingAdapters {

    companion object{

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view:FloatingActionButton,navigate:Boolean){
            view.setOnClickListener {
                if (navigate){
                    view.findNavController().navigate(ListFragmentDirections.actionListFragmentToAddFragment())
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view:View , emptyDatabase : MutableLiveData<Boolean>){
            when(emptyDatabase.value){
                true -> { view.visibility = View.VISIBLE }
                false -> { view.visibility = View.INVISIBLE }
                else -> { view.visibility = View.INVISIBLE }
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view:Spinner , priority: Priority){
            when(priority){
                Priority.HIGH -> { view.setSelection(0) }
                Priority.MEDIUM -> { view.setSelection(1) }
                Priority.LOW -> { view.setSelection(2) }
            }
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(view:CardView,priority: Priority){
            when(priority){
                Priority.HIGH -> { view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.red)) }
                Priority.MEDIUM -> { view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.yellow)) }
                Priority.LOW -> { view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.green)) }
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view:ConstraintLayout,currentItem : ToDoData){
            view.setOnClickListener {
                view.findNavController().navigate(ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem))
            }
        }
    }

}