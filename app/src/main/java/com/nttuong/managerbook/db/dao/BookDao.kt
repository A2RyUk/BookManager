package com.nttuong.managerbook.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nttuong.managerbook.db.entities.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book_table order by bookId ASC")
    fun getAllBooks(): LiveData<List<Book>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)
}