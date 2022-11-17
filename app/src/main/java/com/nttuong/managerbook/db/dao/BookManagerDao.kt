package com.nttuong.managerbook.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.db.relationship.AuthorAndBook
import com.nttuong.managerbook.db.relationship.BookAndChapter
import com.nttuong.managerbook.db.relationship.CategoryAndBook
import kotlinx.coroutines.flow.Flow

@Dao
interface BookManagerDao {

    //Book
    @Query("SELECT * FROM book_table order by bookId ASC")
    fun getAllBooks(): LiveData<List<Book>>
    //get list book order by update date
    @Query("SELECT * FROM book_table ORDER BY `update` DESC")
    fun getAllBookOrderByUpdateDate(): LiveData<List<Book>>
    //get list book order by date
    @Query("SELECT * FROM book_table ORDER BY post DESC")
    fun getAllBookOrderByPostDate(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Query("DELETE FROM book_table WHERE bookId = :id")
    suspend fun deleteBookById(id: Int)


    //Category dao
    @Query("SELECT * FROM category_table order by categoryId ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("DELETE FROM category_table WHERE categoryId = :id")
    suspend fun deleteCategoryById(id: Int)


    //Author Dao
    @Query("SELECT * FROM author_table order by authorId ASC")
    fun getAllAuthors(): LiveData<List<Author>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: Author)

    @Update
    suspend fun updateAuthor(author: Author)

    @Query("DELETE FROM author_table WHERE authorId = :id")
    suspend fun deleteAuthorById(id: Int)


    //Chapter Dao
    @Query("SELECT * FROM chapter_table order by chapterId ASC")
    fun getAllChapters(): LiveData<List<Chapter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Query("DELETE FROM chapter_table WHERE chapName = :name")
    suspend fun deleteChapterByName(name: String)

    //get all chapter by book
    @Query("SELECT * FROM chapter_table WHERE bookName = :name")
    fun getAllChaptersByName(name: String) : Flow<List<Chapter>>
    //get all book by category
    @Query("SELECT * FROM book_table WHERE categoryName = :category")
    fun getAllBookByCategory(category: String) : Flow<List<Book>>

    //search
    @Query("SELECT * FROM book_table WHERE bookName LIKE :searchQuery OR authorName LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): Flow<List<Book>>
    //filter by category
    @Query("SELECT * FROM book_table WHERE categoryName LIKE :categoryName")
    fun searchByCategory(categoryName: String): Flow<List<Book>>
    //get book by name
    @Query("SELECT * FROM book_table WHERE bookName LIKE :bookName")
    fun getBookByName(bookName: String) : Book
    //get chapter by chapter number
    @Query("SELECT * FROM chapter_table WHERE chapNumber LIKE :number")
    suspend fun getChapterByNumber(number: Int) : Chapter
}