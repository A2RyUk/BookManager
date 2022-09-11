package com.nttuong.managerbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nttuong.managerbook.db.dao.AuthorDao
import com.nttuong.managerbook.db.entities.Author

@Database(entities = [Author::class], version = 1, exportSchema = false)
abstract class AuthorDatabase: RoomDatabase() {
    abstract fun getAuthorDao(): AuthorDao

    companion object {
        @Volatile
        var INSTANCE: AuthorDatabase? = null

        fun getAuthorDataBase(context: Context): AuthorDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuthorDatabase::class.java,
                    "authorDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}