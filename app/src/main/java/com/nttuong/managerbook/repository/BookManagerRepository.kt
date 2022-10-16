package com.nttuong.managerbook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nttuong.managerbook.db.dao.BookManagerDao
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.db.relationship.AuthorAndBook
import com.nttuong.managerbook.db.relationship.BookAndChapter
import com.nttuong.managerbook.db.relationship.CategoryAndBook
import kotlinx.coroutines.flow.Flow

class BookManagerRepository(private val bookManagerDao: BookManagerDao) {
    val getAllBooks: LiveData<List<Book>> = bookManagerDao.getAllBooks()
    val getAllAuthors: LiveData<List<Author>> = bookManagerDao.getAllAuthors()
    val getAllCategories: LiveData<List<Category>> = bookManagerDao.getAllCategories()
    val getAllChapters: LiveData<List<Chapter>> = bookManagerDao.getAllChapters()

    //book
    suspend fun insertBook(book : Book) {
        bookManagerDao.insertBook(book = book)
    }

    suspend fun updateBook(book : Book) {
        bookManagerDao.updateBook(book = book)
    }

    suspend fun deleteBookById(id: Int) {
        bookManagerDao.deleteBookById(id = id)
    }

    //category
    suspend fun insertCategory(category: Category) {
        bookManagerDao.insertCategory(category = category)
    }

    suspend fun updateCategory(category: Category) {
        bookManagerDao.updateCategory(category = category)
    }

    suspend fun deleteCategoryById(id: Int) {
        bookManagerDao.deleteCategoryById(id = id)
    }

    //author
    suspend fun insertAuthor(author: Author) {
        bookManagerDao.insertAuthor(author = author)
    }

    suspend fun updateAuthor(author: Author) {
        bookManagerDao.updateAuthor(author = author)
    }

    suspend fun deleteAuthorById(id: Int) {
        bookManagerDao.deleteAuthorById(id = id)
    }

    //chapter
    suspend fun insertChapter(chapter: Chapter) {
        bookManagerDao.insertChapter(chapter = chapter)
    }

    suspend fun updateChapter(chapter: Chapter) {
        bookManagerDao.updateChapter(chapter = chapter)
    }

    suspend fun deleteChapterByName(name: String) {
        bookManagerDao.deleteChapterByName(name = name)
    }

    //relationship
    fun getListChaptersOfBook(bookName: String) : List<BookAndChapter> {
        return bookManagerDao.getListChaptersOfBook(bookName)
    }

    fun getListBooksOfCategory(categoryName: String) : List<CategoryAndBook> {
        return bookManagerDao.getListBooksOfCategory(categoryName)
    }

    fun getListBooksOfAuthor(authorName: String) : List<AuthorAndBook> {
        return bookManagerDao.getListBooksOfAuthor(authorName)
    }

    //search function
    fun searchBook(query: String) : Flow<List<Book>> {
        return bookManagerDao.searchDataBase(query)
    }
    //filter by category
    fun searchByCategory(categoryName: String) : Flow<List<Book>> {
        return bookManagerDao.searchByCategory(categoryName)
    }
    //get all chap by book name
    fun getAllChapterByBookName(name: String) : Flow<List<Chapter>> {
        return bookManagerDao.getAllChaptersByName(name)
    }
}