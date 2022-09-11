package com.nttuong.managerbook.repository

import androidx.lifecycle.LiveData
import com.nttuong.managerbook.db.dao.AuthorDao
import com.nttuong.managerbook.db.entities.Author

class AuthorRepository(private val authorDao: AuthorDao) {
    val getAllAuthors: LiveData<List<Author>> = authorDao.getAllAuthors()

    suspend fun insertAuthor(author: Author){
        authorDao.insert(author = author)
    }

    suspend fun updateAuthor(author: Author){
        authorDao.update(author = author)
    }

    suspend fun deleteAuthor(author: Author){
        authorDao.delete(author = author)
    }
}