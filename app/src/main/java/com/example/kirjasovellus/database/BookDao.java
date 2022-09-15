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


    // Sorted queries title
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY title ASC")
    Book[] getBookOnTitleSortedOnTitleAsc(String title);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY title DESC")
    Book[] getBookOnTitleSortedOnTitleDesc(String title);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY BookId ASC")
    Book[] getBookOnTitleSortedOnIdAsc(String title);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' ORDER BY BookId DESC")
    Book[] getBookOnTitleSortedOnIdDesc(String title);


    // Sorted queries title and genre
    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY title ASC")
    Book[] getBookOnTitleAndGenreIdSortedOnTitleAsc(String title, int id);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY title DESC")
    Book[] getBookOnTitleAndGenreIdSortedOnTitleDesc(String title, int id);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY BookId ASC")
    Book[] getBookOnTitleAndGenreIdSortedOnIdAsc(String title, int id);

    @Query("SELECT * FROM book WHERE title LIKE '%' || :title || '%' AND GenreIds LIKE '%' || :id || ',%' ORDER BY BookId DESC")
    Book[] getBookOnTitleAndGenreIdSortedOnIdDesc(String title, int id);


    @Query("DELETE FROM book")
    public void nukeTable();

    @Query("DELETE FROM book WHERE BookId = :id")
    int deleteBook(int id);
}
