package com.nttuong.managerbook.db.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Chapter

data class BookAndChapter(
    @Embedded val book: Book,

    @Relation(
        parentColumn = "bookName",
        entityColumn = "bookName"
    )
    val chapters: List<Chapter>
) {

}