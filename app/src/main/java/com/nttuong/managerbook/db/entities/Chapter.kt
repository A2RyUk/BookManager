package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class Chapter(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookId") val bookId: Int?,
    @ColumnInfo(name = "chapNumber") val chapNumber: Int?,
    @ColumnInfo(name = "chapName") val chapName: String?,
    @ColumnInfo(name = "content") val content: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chapter

        if (bookId != other.bookId) return false

        return true
    }

    override fun hashCode(): Int {
        return bookId ?: 0
    }
}