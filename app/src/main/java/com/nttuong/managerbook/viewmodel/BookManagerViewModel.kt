package com.nttuong.managerbook.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
    var getAllChapters: LiveData<List<Chapter>>

    var bookManagerRepository: BookManagerRepository

    init {
        val bookManagerDao = BookManagerDataBase.getDatabase(application).getBookManagerDao()
        bookManagerRepository = BookManagerRepository(bookManagerDao,)
        getAllBookList = bookManagerRepository.getAllBooks
        getAllAuthorList = bookManagerRepository.getAllAuthors
        getAllCategoryList = bookManagerRepository.getAllCategories
        getAllChapters = bookManagerRepository.getAllChapters
    }

    //get all category to arrayString
    fun getAllCategoryToString(listCategory: List<Category>): List<String> {
        var listStringCategory = arrayListOf<String>()
        for (i in listCategory.indices) {
            listStringCategory.add(listCategory[i].categoryName.toString())
        }
        return listStringCategory
    }

    fun getCompleteBook(listAllBook: List<Book>) : List<Book> {
        var completeBook = arrayListOf<Book>()
        for (i in listAllBook.indices) {
            if (listAllBook[i].status == "Hoàn Thành") {
                completeBook.add(listAllBook[i])
            }
        }
        return completeBook
    }

    //Basic function insert delete edit
    //Book
    fun insertBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.insertBook(book)
    }
    fun editBook(book: Book) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateBook(book)
    }
    fun deleteBook(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteBookById(id)
    }

    //Author
    fun insertAuthor(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.insertAuthor(author)
    }
    fun editAuthor(author: Author) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateAuthor(author)
    }
    fun deleteAuthor(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteAuthorById(id)
    }

    //Category
    fun insertCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.insertCategory(category)
    }
    fun editCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateCategory(category)
    }
    fun deleteCategory(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteCategoryById(id)
    }

    //Category
    fun insertChapter(chapter: Chapter) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.insertChapter(chapter)
    }
    fun editChapter(chapter: Chapter) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateChapter(chapter)
    }
    fun deleteChapter(name: String) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteChapterByName(name)
    }
}
