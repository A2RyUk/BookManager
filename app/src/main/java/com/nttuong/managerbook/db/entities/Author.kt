package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "author_table")
data class Author(
    @PrimaryKey(autoGenerate = true) var authorId: Int?,
    @ColumnInfo(name = "avatar") var authorAvatar: String? = "",
    @ColumnInfo(name = "authorName") var authorName: String? = "",
    @ColumnInfo(name = "numberOfBook") var numberOfBook: Int? = 0
)
