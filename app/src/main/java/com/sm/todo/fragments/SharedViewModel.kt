package com.sm.todo.fragments

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sm.todo.R
import com.sm.todo.data.models.Priority
import com.sm.todo.data.models.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    /** =======================(List Fragment)============================ */

    val emptyDatabase : MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(data : List<ToDoData>){
        emptyDatabase.value = data.isEmpty()
    }

    /** ====================(Add/Update Fragment)========================= */

    val listener : AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when(position){
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green)) }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    fun parsePriority(priority: String) : Priority {
        return when(priority){
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }

    fun notifyToCompleteData(title: String, description: String , context: Context) {
        if (title.isEmpty()){
            Toast.makeText(context, "Title can not be empty", Toast.LENGTH_SHORT).show()
        }else if (description.isEmpty()){
            Toast.makeText(context, "Description can not be empty", Toast.LENGTH_SHORT).show()
        }
    }

}