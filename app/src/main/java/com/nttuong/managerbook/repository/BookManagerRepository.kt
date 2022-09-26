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

class BookManagerRepository(private val bookManagerDao: BookManagerDao) {
    val getAllBooks: LiveData<List<Book>> = bookManagerDao.getAllBooks()
    val getAllAuthors: LiveData<List<Author>> = bookManagerDao.getAllAuthors()
    val getAllCategories: LiveData<List<Category>> = bookManagerDao.getAllCategories()
    val getAllChapters: LiveData<List<Chapter>> = bookManagerDao.getAllChapters()

    //book repo
    suspend fun insertBook(book : Book) {
        bookManagerDao.insertBook(book = book)
    }

    suspend fun updateBook(book : Book) {
        bookManagerDao.updateBook(book = book)
    }

    suspend fun deleteBook(book : Book) {
        bookManagerDao.deleteBook(book = book)
    }

    //category repo
    suspend fun insertCategory(category: Category) {
        bookManagerDao.insertCategory(category = category)
    }

    suspend fun updateCategory(category: Category) {
        bookManagerDao.updateCategory(category = category)
    }

    suspend fun deleteCategory(category: Category) {
        bookManagerDao.deleteCategory(category = category)
    }

    //author repo
    suspend fun insertAuthor(author: Author){
        bookManagerDao.insertAuthor(author = author)
    }

    suspend fun updateAuthor(author: Author){
        bookManagerDao.updateAuthor(author = author)
    }

    suspend fun deleteAuthor(author: Author){
        bookManagerDao.deleteAuthor(author = author)
    }

    //chapter repo
    suspend fun insertChapter(chapter: Chapter) {
        bookManagerDao.insertChapter(chapter = chapter)
    }

    suspend fun updateChapter(chapter: Chapter) {
        bookManagerDao.updateChapter(chapter = chapter)
    }

    suspend fun deleteChapter(chapter: Chapter) {
        bookManagerDao.deleteChapter(chapter = chapter)
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
}