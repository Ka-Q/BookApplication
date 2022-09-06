package com.example.kirjasovellus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Book... books);

    @Query("SELECT * FROM book")
    Book[] getAllBooks();

    @Query("SELECT * FROM book WHERE BookId = :id")
    Book[] getBookOnId(int id);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%'")
    Book[] getBookOnTitle(String title);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%'")
    Book[] getBookOnTitleAndGenreId(String title, int id);

    @Query("DELETE FROM book")
    public void nukeTable();

}
