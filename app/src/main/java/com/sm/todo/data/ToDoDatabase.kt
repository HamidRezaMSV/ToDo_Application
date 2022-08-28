package com.sm.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sm.todo.data.models.ToDoData

@Database(entities = [ToDoData::class] , version = 1 , exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao() : ToDoDao

    companion object{

        @Volatile // Made visible to other threads
        private var INSTANCE : ToDoDatabase? = null

        fun getDatabase(context: Context) : ToDoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            /* synchronized :
               lock the block so other threads can not call this block
               until this previous thread do it's work completely   */

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext ,
                    ToDoDatabase::class.java ,
                    "todo_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }

}