package com.nttuong.managerbook.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int?,
    @ColumnInfo(name = "categoryName") var categoryName: String? = "",
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (categoryId != other.categoryId) return false

        return true
    }

    override fun hashCode(): Int {
        return categoryId ?: 0
    }
}