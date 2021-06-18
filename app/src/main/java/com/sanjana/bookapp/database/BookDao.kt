package com.sanjana.bookapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT * FROM book")
    fun getAllBook() : List<BookEntity>

    //fun to check whether a particular book is added to favourite or not that is being checked by the book_id that it is added or not
    @Query("SELECT * FROM book WHERE book_id= :bookId")
    fun getBookById(bookId : String) : BookEntity
}