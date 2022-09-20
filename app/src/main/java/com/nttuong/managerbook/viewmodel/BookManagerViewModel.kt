package com.nttuong.managerbook.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nttuong.managerbook.db.BookManagerDataBase
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.db.relationship.AuthorAndBook
import com.nttuong.managerbook.db.relationship.CategoryAndBook
import com.nttuong.managerbook.repository.BookManagerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookManagerViewModel(application: Application): AndroidViewModel(application) {
    var getAllBookList: LiveData<List<Book>>
    var getAllAuthorList: LiveData<List<Author>>
    var getAllCategoryList: LiveData<List<Category>>
    var getAllChapterById: LiveData<List<Chapter>>

    var bookManagerRepository: BookManagerRepository

    init {
        val bookManagerDao = BookManagerDataBase.getDatabase(application).getBookManagerDao()
        bookManagerRepository = BookManagerRepository(bookManagerDao,)
        getAllBookList = bookManagerRepository.getAllBooks
        getAllAuthorList = bookManagerRepository.getAllAuthors
        getAllCategoryList = bookManagerRepository.getAllCategories
        getAllChapterById = bookManagerRepository.getAllChapters
    }

    //Author
    //getNumberOfBook when add Author
    fun getBookNumberAndInsertAuthor(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        author.numberOfBook = 0
        var listAuthorAndBook : List<AuthorAndBook> = bookManagerRepository.getListBooksOfAuthor(author.authorName!!)
        for (aab in listAuthorAndBook) {
            author.numberOfBook = aab.books.size
        }
        bookManagerRepository.insertAuthor(author)
    }
    //delete author
    fun authorDelete(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteAuthor(author)
    }
    //edit author and update book number
    fun authorUpdate(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        author.numberOfBook = 0
        var listAuthorAndBook : List<AuthorAndBook> = bookManagerRepository.getListBooksOfAuthor(author.authorName!!)
        for (aab in listAuthorAndBook) {
            author.numberOfBook = aab.books.size
        }
        bookManagerRepository.updateAuthor(author)
    }

    //Book
    //updateAuthor when insertBook
    fun insertBookAndUpdateAuthorAndCategory(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.insertBook(book)
        var listAuthorAndBook : List<AuthorAndBook> = bookManagerRepository.getListBooksOfAuthor(book.author!!)
        for (aab in listAuthorAndBook) {
            aab.author.numberOfBook = aab.books.size
            bookManagerRepository.updateAuthor(aab.author)
        }
        var listCategoryAndBook : List<CategoryAndBook> = bookManagerRepository.getListBooksOfCategory(book.category!!)
        for (cab in listCategoryAndBook) {
            cab.category.bookNumber = cab.books.size
            bookManagerRepository.updateCategory(cab.category)
        }
    }
    //updateAuthor when deleteBook
    fun deleteBookAndUpdateAuthorAndCategory(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteBook(book)
        var listAuthorAndBook : List<AuthorAndBook> = bookManagerRepository.getListBooksOfAuthor(book.author!!)
        for (aab in listAuthorAndBook) {
            aab.author.numberOfBook = aab.books.size
            bookManagerRepository.updateAuthor(aab.author)
        }
        var listCategoryAndBook : List<CategoryAndBook> = bookManagerRepository.getListBooksOfCategory(book.category!!)
        for (cab in listCategoryAndBook) {
            cab.category.bookNumber = cab.books.size
            bookManagerRepository.updateCategory(cab.category)
        }
    }
    //updateAuthor when updateBook
    fun editBookAndUpdateAuthorAndCategory(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateBook(book)
        var listAuthorAndBook : List<AuthorAndBook> = bookManagerRepository.getListBooksOfAuthor(book.author!!)
        for (aab in listAuthorAndBook) {
            aab.author.numberOfBook = aab.books.size
            bookManagerRepository.updateAuthor(aab.author)
        }
        var listCategoryAndBook : List<CategoryAndBook> = bookManagerRepository.getListBooksOfCategory(book.category!!)
        for (cab in listCategoryAndBook) {
            cab.category.bookNumber = cab.books.size
            bookManagerRepository.updateCategory(cab.category)
        }
    }

    //category
    //getNumberOfBook when add Category
    fun getBookNumberAndInsertCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        category.bookNumber = 0
        var listCategoryAndBook : List<CategoryAndBook> = bookManagerRepository.getListBooksOfCategory(category.categoryName!!)
        for (cab in listCategoryAndBook) {
            category.bookNumber = cab.books.size
        }
        bookManagerRepository.insertCategory(category)
    }
    //edit category and update book number
    fun categoryUpdate(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        category.bookNumber = 0
        var listCategoryAndBook : List<CategoryAndBook> = bookManagerRepository.getListBooksOfCategory(category.categoryName!!)
        for (cab in listCategoryAndBook) {
            category.bookNumber = cab.books.size
        }
        bookManagerRepository.updateCategory(category)
    }
    //delete category
    fun categoryDelete(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteCategory(category)
    }

    //chapter
    fun chapterInsert(chapter: Chapter) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.insertChapter(chapter)
    }

    fun chapterUpdate(chapter: Chapter) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateChapter(chapter)
    }

    fun chapterDelete(chapter: Chapter) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteChapter(chapter)
    }
}
