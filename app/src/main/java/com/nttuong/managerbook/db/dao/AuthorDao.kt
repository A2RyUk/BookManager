package com.nttuong.managerbook.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nttuong.managerbook.db.entities.Author

@Dao
interface AuthorDao {
    @Query("SELECT * FROM author_table order by authorId ASC")
    fun getAllAuthors(): LiveData<List<Author>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(author: Author)

    @Update
    suspend fun update(author: Author)

    @Delete
    suspend fun delete(author: Author)
}