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

    //Book dao
    @Query("SELECT * FROM book_table order by bookId ASC")
    fun getAllBooks(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    //Category dao
    @Query("SELECT * FROM category_table order by categoryId ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    //Author Dao
    @Query("SELECT * FROM author_table order by authorId ASC")
    fun getAllAuthors(): LiveData<List<Author>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: Author)

    @Update
    suspend fun updateAuthor(author: Author)

    @Delete
    suspend fun deleteAuthor(author: Author)


    //Chapter Dao
    @Query("SELECT * FROM chapter_table order by chapterId ASC")
    fun getAllChapters(): LiveData<List<Chapter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Delete
    suspend fun deleteChapter(chapter: Chapter)
}