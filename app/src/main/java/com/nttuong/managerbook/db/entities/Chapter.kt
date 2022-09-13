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

}