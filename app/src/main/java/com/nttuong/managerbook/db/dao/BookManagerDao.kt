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

    @Transaction
    @Query("SELECT * FROM book_table WHERE bookName = :bookName")
    fun getListChaptersOfBook(bookName: String): List<BookAndChapter>

    @Transaction
    @Query("SELECT * FROM category_table WHERE categoryName = :categoryName")
    fun getListBooksOfCategory(categoryName: String): List<CategoryAndBook>

    @Transaction
    @Query("SELECT * FROM author_table WHERE authorName = :authorName")
    fun getListBooksOfAuthor(authorName: String): List<AuthorAndBook>


    //Book
    @Query("SELECT * FROM book_table order by bookId ASC")
    fun getAllBooks(): LiveData<List<Book>>

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

    //search
    @Query("SELECT * FROM book_table WHERE bookName LIKE :searchQuery OR authorName LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): Flow<List<Book>>
    //filter by category
    @Query("SELECT * FROM book_table WHERE categoryName LIKE :categoryName")
    fun searchByCategory(categoryName: String): Flow<List<Book>>
}