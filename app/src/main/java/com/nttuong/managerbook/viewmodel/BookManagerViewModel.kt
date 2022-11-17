package com.nttuong.managerbook.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.nttuong.managerbook.db.BookManagerDataBase
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Category
import com.nttuong.managerbook.db.entities.Chapter
import com.nttuong.managerbook.repository.BookManagerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

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
    var bookFilter = MutableLiveData(FilterType.NONE)

    var bookManagerRepository: BookManagerRepository

    init {
        val bookManagerDao = BookManagerDataBase.getDatabase(application).getBookManagerDao()
        bookManagerRepository = BookManagerRepository(bookManagerDao)
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

    //all favorite book with user
    //get list favorite book name of user
    var bookName = MutableLiveData<ArrayList<String>>()

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

    suspend fun getReadToChapter(chapNumber: Int) : Chapter {
        return bookManagerRepository.getChapterByNumber(chapNumber)
    }

    fun addNewAndDeleteOldBook(readRecentDB: DocumentReference, document: DocumentSnapshot?, bookName: String?) {
        var book1 = document!!.getString("Book1")
        var book2 = document!!.getString("Book2")
        var book3 = document!!.getString("Book3")
        var book4 = document!!.getString("Book4")
        var book5 = document!!.getString("Book5")
        if (bookName != book1 &&
            bookName != book2 &&
            bookName != book3 &&
            bookName != book4 &&
            bookName != book5) {
            book5 = book4
            book4 = book3
            book3 = book2
            book2 = book1
            book1 = bookName
            var b1 = hashMapOf("Book1" to "$book1")
            var b2 = hashMapOf("Book1" to "$book2")
            var b3 = hashMapOf("Book1" to "$book3")
            var b4 = hashMapOf("Book1" to "$book4")
            var b5 = hashMapOf("Book1" to "$book5")
            readRecentDB.set(b1, SetOptions.merge())
            readRecentDB.set(b2, SetOptions.merge())
            readRecentDB.set(b3, SetOptions.merge())
            readRecentDB.set(b4, SetOptions.merge())
            readRecentDB.set(b5, SetOptions.merge())
        }
    }

    fun addAndCreateField(readRecentDB: DocumentReference, bookName: String?) {
        val book1 = hashMapOf("Book1" to "$bookName")
        val book2 = hashMapOf("Book2" to "0")
        val book3 = hashMapOf("Book3" to "0")
        val book4 = hashMapOf("Book4" to "0")
        val book5 = hashMapOf("Book5" to "0")
        readRecentDB.set(book1, SetOptions.merge())
        readRecentDB.set(book2, SetOptions.merge())
        readRecentDB.set(book3, SetOptions.merge())
        readRecentDB.set(book4, SetOptions.merge())
        readRecentDB.set(book5, SetOptions.merge())
    }

    fun getAllFavoriteBook(books: List<Book>, favoriteBooksName: ArrayList<String>) : List<Book> {
        var favoriteBooks = arrayListOf<Book>()
        for (i in 0 until books.size) {
            for (j in 0 until favoriteBooksName.size){
                if (books[i].name == favoriteBooksName[j]) {
                    favoriteBooks.add(books[i])
                }
            }
        }
        return favoriteBooks
    }
}
