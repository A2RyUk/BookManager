package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = true) val bookId: Int?,
    @ColumnInfo(name = "avatar") var avatar: String?,
    @ColumnInfo(name = "bookName") var name: String?,
    @ColumnInfo(name = "authorName") var author: String?,
    @ColumnInfo(name = "categoryName") var category: String?,
    @ColumnInfo(name = "status") var status: String?,
    @ColumnInfo(name = "content") var content: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (bookId != other.bookId) return false

        return true
    }

    override fun hashCode(): Int {
        return bookId ?: 0
    }
}
