package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = true) val bookId: Int?,
    @ColumnInfo(name = "avatar") val avatar: String? = "",
    @ColumnInfo(name = "name") val name: String? = "",
    @ColumnInfo(name = "author") val author: String? = "",
    @ColumnInfo(name = "category") val category: String? = "",
    @ColumnInfo(name = "status") val status: String? = "",
    @ColumnInfo(name = "content") val content: String? = "",
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
