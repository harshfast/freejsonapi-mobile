package com.tak.freeapi.cache.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "table_categories")
class ApiCategory(
        @PrimaryKey(autoGenerate = true) var categoryId: Int = 0,
        @ColumnInfo(name = "category_name") var categoryName: String? = "",
        @ColumnInfo(name = "api_response") var apiResponse: String? = ""
)