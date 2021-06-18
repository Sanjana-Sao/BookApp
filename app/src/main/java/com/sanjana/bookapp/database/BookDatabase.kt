package com.sanjana.bookapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class],version = 1)
abstract class BookDatabase : RoomDatabase() {

    //this allow the all implementation of function of class BookDao
    abstract fun bookDao(): BookDao
}