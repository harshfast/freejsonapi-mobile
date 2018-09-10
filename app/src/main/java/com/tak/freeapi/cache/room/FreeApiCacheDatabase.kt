package com.tak.freeapi.cache.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = [ApiCategory::class], version = 1, exportSchema = false)
abstract class FreeApiCacheDatabase : RoomDatabase() {

    abstract fun getCategoriesDao(): CategoryDao

    companion object {

        private const val DATABASE_NAME = "api_cache.db"
        private var dbInstance: FreeApiCacheDatabase? = null


        fun initDatabase(context: Context) = dbInstance ?: synchronized(this) {
            dbInstance ?: buildDbInstance(context).also { dbInstance = it }
        }

        fun getInstance(): FreeApiCacheDatabase {
            return dbInstance
                    ?: throw RuntimeException("Database is not initialized. Call initDatabase()")
        }

        private fun buildDbInstance(context: Context) = Room.databaseBuilder(context.applicationContext,
                FreeApiCacheDatabase::class.java, DATABASE_NAME)
                .build()

    }
}