package com.nttuong.managerbook.db.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Book

//Author 1-n Books
data class AuthorAndBook(
    @Embedded val author: Author,

    @Relation(
        parentColumn = "authorName",
        entityColumn = "authorName"
    )
    val books: List<Book>
) {
}