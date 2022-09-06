package com.example.kirjasovellus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.kirjasovellus.database.Book;
import com.example.kirjasovellus.database.BookDao;
import com.example.kirjasovellus.database.BookDatabase;
import com.example.kirjasovellus.database.Genre;
import com.example.kirjasovellus.database.GenreDao;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static BookDatabase bookDatabase;
    public static FragmentManager fragmentManager;


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
        //generateTestData(bookDao, genreDao);

        //Get a global FragmentManager
        fragmentManager = getSupportFragmentManager();
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

        Genre math = new Genre();
        math.genreId = 2;
        math.name = "Mathematics";
        math.symbol = "🗿";

        Genre sci = new Genre();
        sci.genreId = 3;
        sci.name = "Science";
        sci.symbol = "🔬";

        Genre fantasy = new Genre();
        fantasy.genreId = 4;
        fantasy.name = "Fantasy";
        fantasy.symbol = "🧙";

        genreDao.insertAll(history, culture, math, sci, fantasy);

        Book historyForNewbies = new Book();
        historyForNewbies.BookId = 0;
        historyForNewbies.title = "History for newbies";
        historyForNewbies.pageCount = 1734;
        historyForNewbies.finished = false;
        historyForNewbies.finishDate = null;
        historyForNewbies.genreIds = new int[]{0,1};

        Book historyOfEarth = new Book();
        historyOfEarth.BookId = 1;
        historyOfEarth.title = "History of Earth";
        historyOfEarth.pageCount = 893;
        historyOfEarth.finished = true;
        historyOfEarth.finishDate = Calendar.getInstance().getTime();
        historyOfEarth.genreIds = new int[]{0};

        Book mathIsFun = new Book();
        mathIsFun.BookId = 2;
        mathIsFun.title = "Math Is Fun!!";
        mathIsFun.pageCount = 152;
        mathIsFun.finished = false;
        mathIsFun.finishDate = null;
        mathIsFun.genreIds = new int[]{2, 3};

        Book encyclopedia = new Book();
        encyclopedia.BookId = 3;
        encyclopedia.title = "Encyclopedia of knowledge";
        encyclopedia.pageCount = 2442;
        encyclopedia.finished = true;
        encyclopedia.finishDate = Calendar.getInstance().getTime();
        encyclopedia.genreIds = new int[]{0, 3};

        Book lotr = new Book();
        lotr.BookId = 4;
        lotr.title = "Lord Of The Rings";
        lotr.pageCount = 768;
        lotr.finished = true;
        lotr.finishDate = Calendar.getInstance().getTime();
        lotr.genreIds = new int[]{4};

        Book algo = new Book();
        algo.BookId = 5;
        algo.title = "Algorithms and you";
        algo.pageCount = 210;
        algo.finished = false;
        algo.finishDate = null;
        algo.genreIds = new int[]{2};

        Book algo2 = new Book();
        algo2.BookId = 6;
        algo2.title = "Algorithms and you but with a long title ";
        algo2.pageCount = 210;
        algo2.finished = false;
        algo2.finishDate = null;
        algo2.genreIds = new int[]{2};

        Book algo3 = new Book();
        algo3.BookId = 7;
        algo3.title = "Algorithms and you but with a longest title am I right? HAHA XDDDDDDDDDDDDD";
        algo3.pageCount = 210;
        algo3.finished = false;
        algo3.finishDate = null;
        algo3.genreIds = new int[]{2};

        Book algo4 = new Book();
        algo4.BookId = 8;
        algo4.title = "Algorithms and you but with a longest title am I right? HAHA XDDDDDDDDDDDDD";
        algo4.pageCount = 210;
        algo4.finished = false;
        algo4.finishDate = null;
        algo4.genreIds = new int[]{2};

        bookDao.insertAll(historyForNewbies, historyOfEarth, mathIsFun, encyclopedia, lotr, algo, algo2, algo3, algo4);

    }
}