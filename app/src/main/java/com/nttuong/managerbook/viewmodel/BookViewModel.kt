package com.nttuong.managerbook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nttuong.managerbook.db.AuthorDatabase
import com.nttuong.managerbook.db.BookDatabase
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.repository.AuthorRepository
import com.nttuong.managerbook.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel(application: Application): AndroidViewModel(application) {
    var getAllBookList: LiveData<List<Book>>
    var bookRepository: BookRepository

    var getAllAuthorList: LiveData<List<Author>>
    var authorRepository: AuthorRepository

    init {
        val dao = BookDatabase.getDatabase(application).getBookDao()
        bookRepository = BookRepository(bookDao = dao)
        getAllBookList = bookRepository.getAllBooks

        val authorDao = AuthorDatabase.getAuthorDataBase(application).getAuthorDao()
        authorRepository = AuthorRepository(authorDao)
        getAllAuthorList = authorRepository.getAllAuthors
    }

    fun bookInsert(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookRepository.insertBook(book)
    }

    fun bookUpdate(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookRepository.updateBook(book)
    }

    fun bookDelete(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookRepository.deleteBook(book)
    }

    fun authorUpdate(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        authorRepository.updateAuthor(author)
    }
}