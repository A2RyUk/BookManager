package com.nttuong.managerbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nttuong.managerbook.db.convert.Converters
import com.nttuong.managerbook.db.dao.BookManagerDao
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.db.entities.Chapter

@Database(
    entities = [
        Book::class,
        Category::class,
        Author::class,
        Chapter::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BookManagerDataBase : RoomDatabase() {
    abstract fun getBookManagerDao(): BookManagerDao

    companion object {
        @Volatile
        var INSTANCE : BookManagerDataBase? = null

        fun getDatabase(context: Context): BookManagerDataBase{
            val tempInstance =  INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookManagerDataBase::class.java,
                    "bookManager_Database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}