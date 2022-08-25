package com.example.kirjasovellus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.BookDao;
import com.example.kirjasovellus.database.BookDatabase;
import com.example.kirjasovellus.database.Genre;
import com.example.kirjasovellus.database.GenreDao;

public class MainActivity extends AppCompatActivity {

    public static BookDatabase bookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create database for books
        bookDatabase = Room.databaseBuilder(
                getApplicationContext(),
                BookDatabase.class,
                "bookDatabase").allowMainThreadQueries().build();
        BookDao bookDao = bookDatabase.bookDao();
        GenreDao genreDao = bookDatabase.genreDao();
        generateTestData(bookDao, genreDao);
    }

    private void generateTestData(BookDao bookDao, GenreDao genreDao) {

        Genre history = new Genre();
        history.genreId = 0;
        history.name = "History";
        history.symbol = "📜";

        Genre culture = new Genre();
        culture.genreId = 1;
        culture.name = "Culture";
        culture.symbol = "🎉";

        genreDao.insertAll(history, culture);

        Book historyForNewbies = new Book();
        historyForNewbies.BookId = 0;
        historyForNewbies.title = "History for newbies";
        historyForNewbies.pageCount = 1734;
        historyForNewbies.finished = false;
        historyForNewbies.finishDate = "25.08.2022";
        historyForNewbies.genreIds = new int[]{0, 1};

        bookDao.insertAll(historyForNewbies);




    }
}