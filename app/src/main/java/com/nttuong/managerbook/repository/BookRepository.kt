package com.nttuong.managerbook.repository

import androidx.lifecycle.LiveData
import com.nttuong.managerbook.db.dao.BookDao
import com.nttuong.managerbook.db.entities.Book

class BookRepository(private val bookDao: BookDao) {

    val getAllBooks: LiveData<List<Book>> = bookDao.getAllBooks()

    suspend fun insertBook(book : Book) {
        bookDao.insert(book = book)
    }

    suspend fun updateBook(book : Book) {
        bookDao.update(book = book)
    }

    suspend fun deleteBook(book : Book) {
        bookDao.delete(book = book)
    }
}