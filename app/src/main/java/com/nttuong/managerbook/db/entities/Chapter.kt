package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter_table")
data class Chapter(
    @PrimaryKey(autoGenerate = true) val chapterId: Int?,
    @ColumnInfo(name = "bookName") var bookName: String?,
    @ColumnInfo(name = "chapNumber") var chapNumber: Int?,
    @ColumnInfo(name = "chapName") var chapName: String?,
    @ColumnInfo(name = "content") var content: String?
) {

}