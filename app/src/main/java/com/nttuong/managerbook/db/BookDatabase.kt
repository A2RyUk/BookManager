package com.nttuong.managerbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nttuong.managerbook.db.dao.BookDao
import com.nttuong.managerbook.db.entities.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase: RoomDatabase() {
    abstract fun getBookDao(): BookDao

    companion object {
        @Volatile
        var INSTANCE : BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase{
            val tempInstance =  INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "bookDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}