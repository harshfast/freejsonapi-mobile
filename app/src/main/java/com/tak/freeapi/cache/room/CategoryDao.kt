package com.tak.freeapi.cache.room

import android.arch.persistence.room.*


@Dao
interface CategoryDao {

    @Query("SELECT * from table_categories")
    fun getAllCategories(): List<ApiCategory>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(apiCategories: List<ApiCategory>)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateApiResponse(apiData: ApiCategory)


    @Query("SELECT * FROM table_categories WHERE category_name = :categoryName")
    fun loadApisForCategory(categoryName: String): ApiCategory

}