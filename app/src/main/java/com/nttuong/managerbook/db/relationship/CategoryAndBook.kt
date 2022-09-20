package com.nttuong.managerbook.db.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Category

//1 Category - n Books
data class CategoryAndBook(
    @Embedded val category: Category,

    @Relation(
        parentColumn = "categoryName",
        entityColumn = "categoryName"
    )
    val books: List<Book>
) {
}