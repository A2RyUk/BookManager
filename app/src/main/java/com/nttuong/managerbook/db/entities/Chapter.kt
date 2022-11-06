package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.type.DateTime
import com.nttuong.managerbook.db.convert.Converters
import java.util.Date

@Entity(tableName = "chapter_table")
data class Chapter(
    @PrimaryKey(autoGenerate = true) val chapterId: Int?,
    @ColumnInfo(name = "bookName") var bookName: String? = "",
    @ColumnInfo(name = "chapNumber") var chapNumber: Int? = null,
    @ColumnInfo(name = "chapName") var chapName: String? = "",
    @ColumnInfo(name = "content") var content: String? = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chapter

        if (chapterId != other.chapterId) return false

        return true
    }

    override fun hashCode(): Int {
        return chapterId ?: 0
    }
}