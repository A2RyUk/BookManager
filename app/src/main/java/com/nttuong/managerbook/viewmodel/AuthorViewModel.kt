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

class AuthorViewModel(application: Application): AndroidViewModel(application) {
    var getAllAuthorList: LiveData<List<Author>>
    var authorRepository: AuthorRepository

    var getAllBookList: LiveData<List<Book>>
    var bookRepository: BookRepository

    init {
        val authorDao = AuthorDatabase.getAuthorDataBase(application).getAuthorDao()
        authorRepository = AuthorRepository(authorDao)
        getAllAuthorList = authorRepository.getAllAuthors

        val bookDao = BookDatabase.getDatabase(application).getBookDao()
        bookRepository = BookRepository(bookDao = bookDao)
        getAllBookList = bookRepository.getAllBooks
    }

    fun authorInsert(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        authorRepository.insertAuthor(author)
    }

    fun authorUpdate(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        authorRepository.updateAuthor(author)
    }

    fun authorDelete(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        authorRepository.deleteAuthor(author)
    }
}