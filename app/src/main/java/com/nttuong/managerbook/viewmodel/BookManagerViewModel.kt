package com.nttuong.managerbook.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.paging.PagedList
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

enum class FilterType {
    NONE, FAVORITES, SEARCH_RESULTS, CATEGORY
}

class BookManagerViewModel(application: Application): AndroidViewModel(application) {
    var getAllBookList: LiveData<List<Book>>
    var getAllAuthorList: LiveData<List<Author>>
    var getAllCategoryList: LiveData<List<Category>>
    var getAllChapters: LiveData<List<Chapter>>
    var getAllBookByUpdateDate: LiveData<List<Book>>
    var getAllBooksByPostDate: LiveData<List<Book>>
    private var searchStr = ""
    private var searchCategory = ""
    private var bookName = MutableLiveData(String)
    var bookFilter = MutableLiveData(FilterType.NONE)
    //account level

    var bookManagerRepository: BookManagerRepository


    init {
        val bookManagerDao = BookManagerDataBase.getDatabase(application).getBookManagerDao()
        bookManagerRepository = BookManagerRepository(bookManagerDao,)
        getAllBookList = bookManagerRepository.getAllBooks
        getAllAuthorList = bookManagerRepository.getAllAuthors
        getAllCategoryList = bookManagerRepository.getAllCategories
        getAllChapters = bookManagerRepository.getAllChapters
        getAllBookByUpdateDate = bookManagerRepository.getAllBooksByUpdateDate
        getAllBooksByPostDate = bookManagerRepository.getAllBooksByPostDate
    }

    private val allBooks = Transformations.switchMap(bookFilter) {
        when (bookFilter.value) {
            FilterType.NONE -> {
                getAllBookList
            }
            FilterType.CATEGORY -> {
                bookManagerRepository.searchByCategory(searchCategory).asLiveData()
            }
            FilterType.SEARCH_RESULTS -> {
                bookManagerRepository.searchBook(searchStr).asLiveData()
            }
            else -> throw IllegalArgumentException("Unknown Filter")
        }
    }

    val books: LiveData<List<Book>>
        get() = allBooks

    fun getAllChaptersByName(name: String) : LiveData<List<Chapter>> {
        return bookManagerRepository.getAllChapterByBookName(name).asLiveData()
    }

    fun getAllBookByCategory(category: String) : LiveData<List<Book>> {
        return  bookManagerRepository.getAllBookByCategory(category).asLiveData()
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

    //Chapter
    fun chapterInsert(chapter: Chapter, bookName: String) = viewModelScope.launch(Dispatchers.IO) {
        chapter.bookName = bookName
        bookManagerRepository.insertChapter(chapter)
    }
    fun editChapter(chapter: Chapter) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.updateChapter(chapter)
    }
    fun deleteChapter(name: String) = viewModelScope.launch(Dispatchers.IO) {
        bookManagerRepository.deleteChapterByName(name)
    }

    fun searchWithText(query : String?) {
        query?.let {
            searchStr = it
        }
        bookFilter.value = FilterType.SEARCH_RESULTS
    }
    fun searchWithCategory(category: String?){
        category?.let {
            searchCategory = it
        }
        bookFilter.value = FilterType.CATEGORY
    }

    fun getNextChapter(chapterName: String, listChapterOfBook: List<Chapter>) : Chapter {
        var currentChapterIndex = 0
        for (i in listChapterOfBook.indices) {
            if (listChapterOfBook[i].chapName == chapterName) {
                currentChapterIndex = i
            }
        }
        var nextChapterIndex = currentChapterIndex?.plus(1)
        Log.d("next", "chap index: $nextChapterIndex ")
        return listChapterOfBook[nextChapterIndex!!]
    }
    fun getPrevChapter(chapterName: String, listChapterOfBook: List<Chapter>) : Chapter {
        var currentChapterIndex = 0
        for (i in listChapterOfBook.indices) {
            if (listChapterOfBook[i].chapName == chapterName) {
                currentChapterIndex = i
            }
        }
        var nextChapterIndex = currentChapterIndex?.minus(1)
        Log.d("next", "chap index: $nextChapterIndex ")
        return listChapterOfBook[nextChapterIndex!!]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTimeWhenAddChapter(bookName: String) = viewModelScope.launch(Dispatchers.IO) {
        val book = bookManagerRepository.getBookByName(bookName)
        book.updateTime = LocalDateTime.now()
        bookManagerRepository.updateBook(book)
    }
}
